package com.atec.learning.classification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;

public class RunnableExample {
	@Resource(name ="blCatalogService")
	protected static  CatalogService catalogservice;

    public static void main(String[] args) {

    	
    	
       
    	final RdrClassificationUtils<Long, Long> bayes =
                new BayesClassifier<Long, Long>();
    
       List<Product> products=catalogservice.findAllProducts();
    	Map<Long, List<Long>> categoryProducts = new HashMap<Long, List<Long>>();
		
		for (Product productId:products)
		{
			
			Product product = catalogservice.findProductById(productId.getId());
			if(Objects.nonNull(product)){
				Category cat = product.getCategory();
				 
				if(categoryProducts.get(cat.getId()) != null){
					List<Long> prods = categoryProducts.get(cat.getId());
					bayes.learn(cat.getId(),prods);///////
						prods.add(productId.getId());
						categoryProducts.put(cat.getId(), prods);
						 System.out.println( // will output "positive"
					                bayes.classify(prods));
						 ((BayesClassifier<Long, Long>) bayes).classifyDetailed(
					                prods);
				} else {
					List<Long> prods = new ArrayList<Long>();
					prods.add(productId.getId());
					categoryProducts.put(cat.getId(), prods);
					 System.out.println( // will output "positive"
				                bayes.classify(prods));
				}
				 bayes.setMemoryCapacity(500); // remember the last 500 learned classifications
}
		}
    }

	}
