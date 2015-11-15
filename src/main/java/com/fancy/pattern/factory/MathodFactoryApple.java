package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.Product;
import com.fancy.pattern.factory.model.ProductApple;

public class MathodFactoryApple extends IMathodFactory {

	@Override
	Product madeProduct() {
		// .....
		return new ProductApple();
	}

}
