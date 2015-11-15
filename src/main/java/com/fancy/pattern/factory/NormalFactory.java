package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.Product;
import com.fancy.pattern.factory.model.ProductApple;

public class NormalFactory {

	public static Product create() {
		Product p = new ProductApple();
		return p;
	}
	
}
