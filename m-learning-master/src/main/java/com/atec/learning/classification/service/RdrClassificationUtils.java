package com.atec.learning.classification.service;


import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Abstract base extended by any concrete classifier.  It implements the basic
 * functionality for storing categories or features and can be used to calculate
 * basic probabilities – both category and feature probabilities. The classify
 * function has to be implemented by the concrete classifier class.
 *
 * 
 *
 * @param <Product> A feature class
 * @param <Category> A category class
 */
public abstract class RdrClassificationUtils<Product, Category> implements IFeatureProbability<Product, Category> {

    /**
     * Initial capacity of category dictionaries.
     */
    private static final int INITIAL_CATEGORY_DICTIONARY_CAPACITY = 16;

    /**
     * Initial capacity of feature dictionaries. It should be quite big, because
     * the features will quickly outnumber the categories. 
     */
    private static final int INITIAL_FEATURE_DICTIONARY_CAPACITY = 32;

    /**
     * The initial memory capacity or how many classifications are memorized.
     */
    private int memoryCapacity = 1000;

    /**
     * A dictionary mapping features to their number of occurrences in each
     * known category.
     */
    private Dictionary<Category, Dictionary<Product, Integer>> featureCountPerCategory;

    /**
     * A dictionary mapping features to their number of occurrences.
     */
    private Dictionary<Product, Integer> totalFeatureCount;

    /**
     * A dictionary mapping categories to their number of occurrences.
     */
    private Dictionary<Category, Integer> totalCategoryCount;

    /**
     * The classifier's memory. It will forget old classifications as soon as
     * they become too old.
     */
    private Queue<RdrClassification<Product, Category>> memoryQueue;

    /**
     * Constructs a new classifier without any trained knowledge.
     */
    public RdrClassificationUtils() {
        this.reset();
    }

    /**
     * Resets the <i>learned</i> feature and category counts.
     */
    public void reset() {
        this.featureCountPerCategory =
                new Hashtable<Category, Dictionary<Product,Integer>>(
                        RdrClassificationUtils.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
        this.totalFeatureCount =
                new Hashtable<Product, Integer>(
                        RdrClassificationUtils.INITIAL_FEATURE_DICTIONARY_CAPACITY);
        this.totalCategoryCount =
                new Hashtable<Category, Integer>(
                        RdrClassificationUtils.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
        this.memoryQueue = new LinkedList<RdrClassification<Product, Category>>();
    }

    /**
     * Returns a <code>Set</code> of features the classifier knows about.
     *
     * @return The <code>Set</code> of features the classifier knows about.
     */
    public Set<Product> getFeatures() {
        return ((Hashtable<Product, Integer>) this.totalFeatureCount).keySet();
    }

    /**
     * Returns a <code>Set</code> of categories the classifier knows about.
     *
     * @return The <code>Set</code> of categories the classifier knows about.
     */
    public Set<Category> getCategories() {
        return ((Hashtable<Category, Integer>) this.totalCategoryCount).keySet();
    }

    /**
     * Retrieves the total number of categories the classifier knows about.
     *
     * @return The total category count.
     */
    public int getCategoriesTotal() {
        int toReturn = 0;
        for (Enumeration<Integer> e = this.totalCategoryCount.elements();
                e.hasMoreElements();) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    /**
     * Retrieves the memory's capacity.
     *
     * @return The memory's capacity.
     */
    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    /**
     * Sets the memory's capacity.  If the new value is less than the old
     * value, the memory will be truncated accordingly.
     *
     * @param memoryCapacity The new memory capacity.
     */
    public void setMemoryCapacity(int memoryCapacity) {
        for (int i = this.memoryCapacity; i > memoryCapacity; i--) {
            this.memoryQueue.poll();
        }
        this.memoryCapacity = memoryCapacity;
    }

    /**
     * Increments the count of a given feature in the given category.  This is
     * equal to telling the classifier, that this feature has occurred in this
     * category.
     *
     * @param feature The feature, which count to increase.
     * @param category The category the feature occurred in.
     */
    public void incrementFeature(Product feature, Category category) {
        Dictionary<Product, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null) {
            this.featureCountPerCategory.put(category,
                    new Hashtable<Product, Integer>(
                            RdrClassificationUtils.INITIAL_FEATURE_DICTIONARY_CAPACITY));
            features = this.featureCountPerCategory.get(category);
        }
        Integer count = features.get(feature);
        if (count == null) {
            features.put(feature, 0);
            count = features.get(feature);
        }
        features.put(feature, ++count);

        Integer totalCount = this.totalFeatureCount.get(feature);
        if (totalCount == null) {
            this.totalFeatureCount.put(feature, 0);
            totalCount = this.totalFeatureCount.get(feature);
        }
        this.totalFeatureCount.put(feature, ++totalCount);
    }

    /**
     * Increments the count of a given category.  This is equal to telling the
     * classifier, that this category has occurred once more.
     *
     * @param category The category, which count to increase.
     */
    public void incrementCategory(Category category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            this.totalCategoryCount.put(category, 0);
            count = this.totalCategoryCount.get(category);
        }
       this.totalCategoryCount.put(category, ++count);
    }

    /**
     * Decrements the count of a given feature in the given category.  This is
     * equal to telling the classifier that this feature was classified once in
     * the category.
     *
     * @param feature The feature to decrement the count for.
     * @param category The category.
     */
    public void decrementFeature(Product feature, Category category) {
        Dictionary<Product, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null) {
            return;
        }
        Integer count = features.get(feature);
        if (count == null) {
            return;
        }
        if (count.intValue() == 1) {
            features.remove(feature);
            if (features.size() == 0) {
                this.featureCountPerCategory.remove(category);
            }
        } else {
            features.put(feature, --count);
        }

        Integer totalCount = this.totalFeatureCount.get(feature);
        if (totalCount == null) {
            return;
        }
        if (totalCount.intValue() == 1) {
            this.totalFeatureCount.remove(feature);
        } else {
            this.totalFeatureCount.put(feature, --totalCount);
        }
    }

    /**
     * Decrements the count of a given category.  This is equal to telling the
     * classifier, that this category has occurred once less.
     *
     * @param category The category, which count to increase.
     */
    public void decrementCategory(Category category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            return;
        }
        if (count.intValue() == 1) {
            this.totalCategoryCount.remove(category);
        } else {
            this.totalCategoryCount.put(category, --count);
        }
    }

    /**
     * Retrieves the number of occurrences of the given feature in the given
     * category.
     *
     * @param feature The feature, which count to retrieve.
     * @param category The category, which the feature occurred in.
     * @return The number of occurrences of the feature in the category.
     */
    public int featureCount(Product feature, Category category) {
        Dictionary<Product, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null)
            return 0;
        Integer count = features.get(feature);
        return (count == null) ? 0 : count.intValue();
    }

    /**
     * Retrieves the number of occurrences of the given category.
     * 
     * @param category The category, which count should be retrieved.
     * @return The number of occurrences.
     */
    public int categoryCount(Category category) {
        Integer count = this.totalCategoryCount.get(category);
        return (count == null) ? 0 : count.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float featureProbability(Product feature, Category category) {
        if (this.categoryCount(category) == 0)
            return 0;
        return (float) this.featureCount(feature, category)
                / (float) this.categoryCount(category);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * overall weight of <code>1.0</code> and an assumed probability of
     * <code>0.5</code>. The probability defaults to the overall feature
     * probability.
     *
     * 
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(Product feature, Category category) {
        return this.featureWeighedAverage(feature, category,
                null, 1.0f, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * overall weight of <code>1.0</code>, an assumed probability of
     * <code>0.5</code> and the given object to use for probability calculation.
     *
    
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(Product feature, Category category,
            IFeatureProbability<Product, Category> calculator) {
        return this.featureWeighedAverage(feature, category,
                calculator, 1.0f, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * the given weight and an assumed probability of <code>0.5</code> and the
     * given object to use for probability calculation.
     *
    
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @param weight The feature weight.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(Product feature, Category category,
            IFeatureProbability<Product, Category> calculator, float weight) {
        return this.featureWeighedAverage(feature, category,
                calculator, weight, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * the given weight, the given assumed probability and the given object to
     * use for probability calculation.
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @param weight The feature weight.
     * @param assumedProbability The assumed probability.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(Product feature, Category category,
            IFeatureProbability<Product, Category> calculator, float weight,
            float assumedProbability) {

        /*
         * use the given calculating object or the default method to calculate
         * the probability that the given feature occurred in the given
         * category.
         */
        final float basicProbability =
                (calculator == null)
                    ? this.featureProbability(feature, category)
                            : calculator.featureProbability(feature, category);

        Integer totals = this.totalFeatureCount.get(feature);
        if (totals == null)
            totals = 0;
        return (weight * assumedProbability + totals  * basicProbability)
                / (weight + totals);
    }

    /**
     * Train the classifier by telling it that the given features resulted in
     * the given category.
     *
     * @param cat The category the features belong to.
     * @param features The features that resulted in the given category.
     */
    public void learn(Category cat, List<Product> features) {
        this.learn(new RdrClassification<Product, Category> (features, cat));
    }

    /**
     * Train the classifier by telling it that the given features resulted in
     * the given category.
     *
     * @param classification The classification to learn.
     */
    public void learn(RdrClassification<Product, Category> classification) {

        for (Product feature : classification.getFeatureset())
            this.incrementFeature(feature, classification.getCategory());
        this.incrementCategory(classification.getCategory());

        this.memoryQueue.offer(classification);
        if (this.memoryQueue.size() > this.memoryCapacity) {
        	RdrClassification<Product, Category>  toForget = this.memoryQueue.remove();

            for (Product feature : toForget.getFeatureset())
                this.decrementFeature(feature, toForget.getCategory());
            this.decrementCategory(toForget.getCategory());
        }
    }

    /**
     * The classify method.  It will retrieve the most likely category for the
     * features given and depends on the concrete classifier implementation.
     *
     * @param features The features to classify.
     * @return The category most likely.
     */
    public abstract RdrClassification<Product, Category> classify(Collection<Product> features);

}