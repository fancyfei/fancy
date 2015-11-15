package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.ProductApple;
import com.fancy.pattern.factory.model.ProductApple4;
import com.fancy.pattern.factory.model.ProductSamsung;
import com.fancy.pattern.factory.model.ProductSamsung4s;

public class AbstractFactoryAS extends IAbstractFactory {

	@Override
	ProductApple createProductApple() {
		return new ProductApple4();
	}

	@Override
	ProductSamsung createProductSamsung() {
		return new ProductSamsung4s();
	}

}
