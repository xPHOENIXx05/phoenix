package eu.kanade.phoenix.migration.service

import eu.kanade.phoenix.migration.model.PhoenixBackup
import mihon.domain.source.interactor.UpdateMangaFromRemote
import tachiyomi.domain.category.interactor.CreateCategoryWithName
import tachiyomi.domain.category.interactor.SetMangaCategories
import tachiyomi.domain.category.repository.CategoryRepository
import tachiyomi.domain.chapter.model.ChapterUpdate
import tachiyomi.domain.chapter.repository.ChapterRepository
import tachiyomi.domain.manga.interactor.NetworkToLocalManga
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import kotlin.math.abs

class MigrationImportService(
    private val networkToLocalManga: NetworkToLocalManga = Injekt.get(),
    private val chapterRepository: ChapterRepository = Injekt.get(),
    private val updateMangaFromRemote: UpdateMangaFromRemote = Injekt.get(),
    private val createCategoryWithName: CreateCategoryWithName = Injekt.get(),
    private val setMangaCategories: SetMangaCategories = Injekt.get(),
    private val categoryRepository: CategoryRepository = Injekt.get(),
) {

    suspend fun importLibrary(
        backup: PhoenixBackup,
    ) {

        // Create categories from backup
        for (category in backup.categories) {
            createCategoryWithName.await(category.name)
        }

        val localCategories = categoryRepository.getAll()


        val categoryOrderMap = backup.categories.associate { backupCategory ->

                val localCategory =
                    localCategories.firstOrNull {
                        it.name == backupCategory.name
                    }

            backupCategory.order to (localCategory?.id ?: 0L)
        }


        val manga = backup.manga.map {
            it.backupManga.getMangaImpl()
        }

        val importedManga = networkToLocalManga(manga)

        check(importedManga.size == backup.manga.size) {
            "Imported manga count doesn't match backup count"
        }

        val importedMap = importedManga.associateBy {
            it.source to it.url
        }

        for (item in backup.manga) {

            val localManga = importedMap[
                item.preview.sourceId to item.preview.url,
            ]

            if (localManga == null) {
                continue
            }

            val mappedCategoryIds =
                item.backupManga.categories.mapNotNull {
                    categoryOrderMap[it]
                }.filter { it != 0L }


            val syncResult = updateMangaFromRemote(
                manga = localManga,
                fetchDetails = false,
                fetchChapters = true,
                manualFetch = true,
            )

            if (syncResult.isFailure) {
                continue
            }

            setMangaCategories.await(
                mangaId = localManga.id,
                categoryIds = mappedCategoryIds,
            )

            val localChapters =
                chapterRepository.getChapterByMangaId(localManga.id)

            for (backupChapter in item.backupManga.chapters) {

                val localChapter =
                    localChapters.firstOrNull {
                        abs(
                            it.chapterNumber - backupChapter.chapterNumber.toDouble(),
                        ) < 0.0001
                    }

                if (localChapter == null) {
                    continue

                }


                chapterRepository.update(
                    ChapterUpdate(
                        id = localChapter.id,
                        read = backupChapter.read,
                        bookmark = backupChapter.bookmark,
                        lastPageRead = backupChapter.lastPageRead,
                    ),
                )

            } // backupChapter loop
        }     // item loop
    }         // importLibrary()
}             // MigrationImportService
