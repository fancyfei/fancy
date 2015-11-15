package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.ProductApple;
import com.fancy.pattern.factory.model.ProductSamsung;

public class AbstractFactorySA extends IAbstractFactory {

	@Override
	ProductApple createProductApple() {
		return null;
	}

	@Override
	ProductSamsung createProductSamsung() {
		return null;
	}

}
