package com.atec.learning.classification.service;


import java.util.Collection;

/**
 * A basic wrapper reflecting a classification.  It will store both featureset
 * and resulting classification.
 *
 * 
 *
 * @param <Product> The feature class.
 * @param <Category> The category class.
 */
public class RdrClassification<Product, Category> {

    /**
     * The classified featureset.
     */
    private Collection<Product> featureset;

    /**
     * The category as which the featureset was classified.
     */
    private Category category;

    /**
     * The probability that the featureset belongs to the given category.
     */
    private float probability;

    /**
     * Constructs a new Classification with the parameters given and a default
     * probability of 1.
     *
     * @param featureset The featureset.
     * @param category The category.
     */
    public RdrClassification(Collection<Product> featureset, Category category) {
        this(featureset, category, 1.0f);
    }

    /**
     * Constructs a new Classification with the parameters given.
     *
     * @param featureset The featureset.
     * @param category The category.
     * @param probability The probability.
     */
    public RdrClassification(Collection<Product> featureset, Category category,
            float probability) {
        this.featureset = featureset;
        this.category = category;
        this.probability = probability;
    }

    /**
     * Retrieves the featureset classified.
     *
     * @return The featureset.
     */
    public Collection<Product> getFeatureset() {
        return featureset;
    }

    /**
     * Retrieves the classification's probability.
     * @return
     */
    public float getProbability() {
        return this.probability;
    }

    /**
     * Retrieves the category the featureset was classified as.
     *
     * @return The category.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Classification [category=" + this.category
                + ", probability=" + this.probability
                + ", featureset=" + this.featureset
                + "]";
    }

}
