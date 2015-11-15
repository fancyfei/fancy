package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.Product;
import com.fancy.pattern.factory.model.ProductSamsung;

public class MathodFactorySumsung extends IMathodFactory {

	@Override
	Product madeProduct() {
		// .....
		return new ProductSamsung();
	}

}
