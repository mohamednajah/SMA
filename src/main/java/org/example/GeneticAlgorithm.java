package org.example;

import java.util.Random;

public class GeneticAlgorithm {
    private static final String TARGET_PHRASE = "bonjour bdcC";
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.01;

    private static final String GENES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    private static final int TARGET_LENGTH = TARGET_PHRASE.length();

    private static Random random = new Random();

    private static char getRandomGene() {
        int index = random.nextInt(GENES.length());
        return GENES.charAt(index);
    }

    private static String generateRandomPhrase() {
        StringBuilder phrase = new StringBuilder(TARGET_LENGTH);
        for (int i = 0; i < TARGET_LENGTH; i++) {
            phrase.append(getRandomGene());
        }
        return phrase.toString();
    }

    private static double calculateFitness(String phrase) {
        double score = 0;
        for (int i = 0; i < TARGET_LENGTH; i++) {
            if (phrase.charAt(i) == TARGET_PHRASE.charAt(i)) {
                score++;
            }
        }
        return score / TARGET_LENGTH;
    }

    private static String mutate(String phrase) {
        StringBuilder mutatedPhrase = new StringBuilder(phrase);
        for (int i = 0; i < TARGET_LENGTH; i++) {
            if (random.nextDouble() <= MUTATION_RATE) {
                mutatedPhrase.setCharAt(i, getRandomGene());
            }
        }
        return mutatedPhrase.toString();
    }

    private static String crossover(String parent1, String parent2) {
        StringBuilder child = new StringBuilder(TARGET_LENGTH);
        int crossoverPoint = random.nextInt(TARGET_LENGTH);
        for (int i = 0; i < TARGET_LENGTH; i++) {
            if (i < crossoverPoint) {
                child.append(parent1.charAt(i));
            } else {
                child.append(parent2.charAt(i));
            }
        }
        return child.toString();
    }

    public static void main(String[] args) {
        String[] population = new String[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = generateRandomPhrase();
        }

        boolean foundSolution = false;
        int generations = 0;

        while (!foundSolution) {
            generations++;

            double[] fitnessScores = new double[POPULATION_SIZE];
            double totalFitness = 0;

            for (int i = 0; i < POPULATION_SIZE; i++) {
                fitnessScores[i] = calculateFitness(population[i]);
                totalFitness += fitnessScores[i];
            }

            // Check if target phrase is found
            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (population[i].equals(TARGET_PHRASE)) {
                    foundSolution = true;
                    System.out.println("Target phrase found in generation " + generations + ": " + population[i]);
                    break;
                }
            }

            // Create the next generation
            String[] newPopulation = new String[POPULATION_SIZE];

            for (int i = 0; i < POPULATION_SIZE; i++) {
                double selectionProbability = fitnessScores[i] / totalFitness;

                // Select parents using roulette wheel selection
                String parent1 = population[selectParent(fitnessScores, totalFitness)];
                String parent2 = population[selectParent(fitnessScores, totalFitness)];

                // Crossover
                String child = crossover(parent1, parent2);

                // Mutation
                child = mutate(child);

                newPopulation[i] = child;
            }

            population = newPopulation;
        }
    }

    private static int selectParent(double[] fitnessScores, double totalFitness) {
        double randomValue = random.nextDouble() * totalFitness;
        double cumulativeFitness = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            cumulativeFitness += fitnessScores[i];
            if (cumulativeFitness >= randomValue) {
                return i;
            }
        }
        return POPULATION_SIZE - 1;
    }
}
