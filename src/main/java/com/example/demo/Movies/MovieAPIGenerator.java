package com.example.demo.Movies;

import com.example.demo.Models.Movie;

import java.util.*;
import java.util.stream.Collectors;

class MovieAPIGenerator extends MovieService {
    private Integer count;

    MovieAPIGenerator(Integer count) {
        this.count = count;

        generateResult(generateWords());
    }

    private void generateResult(List<String> randomTitles) {
        for (String title :
                    randomTitles) {
            validateResults(title);
        }
    }

    private void validateResults(String title) {
        connector = new Connector("s", title);

        System.out.println(title);
        for(Movie movie :
                connector.getMovies())
        {
            System.out.println(movie.getTitle());
            System.out.println(movie.getPoster());
        }

        List<Movie> result = deleteMoviesWithoutPosters(connector.getMovies());
        System.out.println(result);

        if (result.isEmpty()) {
            String newTitle = generateOneWord();
            validateResults(newTitle);
        } else {
            System.out.println(result.get(0));
            resultList.add(result.get(0));
        }
    }

    private String generateOneWord() {
        FileLinesLoader fl = new FileLinesLoader();
        List<String> randomTitle = fl
                .loadLinesShuffled("wordsbank")
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("New fixed Title " + randomTitle.get(5));

        return randomTitle.get(5);
    }

    private List<String> generateWords() {
        FileLinesLoader fl = new FileLinesLoader();
        Set<String> randomWords = fl
                .loadLinesShuffled("wordsbank")
                .stream()
                .limit(count)
                .collect(Collectors.toSet());

        for (String checkingWord :
                    randomWords){
            if(checkingWord.contains(" ")){
                randomWords.remove(checkingWord);
                randomWords.add(checkingWord.replaceAll(" ", "%20"));

                System.out.println("Fixed: " + checkingWord);
            }
        }

        return new ArrayList<>(randomWords);
    }

}
