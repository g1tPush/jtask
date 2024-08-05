package com.task.jtask.service.impl;

import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.model.TranslationRecord;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.repository.TranslationRepository;
import com.task.jtask.dto.TranslationResponseDto;
import com.task.jtask.service.TranslationRequestService;
import com.task.jtask.service.TranslationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRequestService translationRequestService;
    private final TranslationRepository translationRepository;
    private final int threadPoolSize = 10;
    private final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

    public TranslationServiceImpl(TranslationRepository translationRepository, TranslationRequestService translationRequestService) {
        this.translationRepository = translationRepository;
        this.translationRequestService = translationRequestService;
    }

    public TranslationRecord handleTranslationRequest(TranslationInputDto translationInputDto, String ipAddress) {
        String translatedText = translateText(translationInputDto);

        TranslationRecord translationRecord = TranslationRecord.builder()
                .ipAddress(ipAddress)
                .originalStringToTranslate(translationInputDto.getText())
                .translatedString(translatedText)
                .build();

        return translationRepository.saveTranslation(translationRecord);
    }

    private String translateText(TranslationInputDto translationInputDto) {
        List<String> words = Arrays.stream(translationInputDto.getText().split(" "))
                .filter(word -> !word.isEmpty())
                .toList();

        List<CompletableFuture<TranslationResponseDto>> futures = new ArrayList<>(Collections.nCopies(words.size(), null));

        for (int i = 0; i < words.size(); i++) {
            TranslationInputDto translationInputWithOneWord = TranslationInputDto.builder()
                    .text(words.get(i))
                    .targetLanguageCode(translationInputDto.getTargetLanguageCode())
                    .sourceLanguageCode(translationInputDto.getSourceLanguageCode())
                    .build();

            futures.set(i, CompletableFuture.supplyAsync(() -> translationRequestService.translate(translationInputWithOneWord), executorService));
        }

        StringBuilder translatedText = new StringBuilder();

        CompletableFuture.allOf(futures.get(0)).join();

        for (CompletableFuture<TranslationResponseDto> future : futures) {
            try {
                translatedText.append(future.get().getTranslations().get(0).getText()).append(" ");
            } catch (InterruptedException | ExecutionException e) {
                throw new GlobalException(e.getMessage());
            }
        }

        return translatedText.toString().trim();
    }

}
