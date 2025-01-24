package com.atec.learning.classification.service;

/**
 * Simple interface defining the method to calculate the feature probability.
 *
 * @author mahbouba
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public interface IFeatureProbability<Product, Category> {

    public float featureProbability(Product feature, Category category);

}