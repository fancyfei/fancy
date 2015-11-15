package com.fancy.pattern.factory;

import com.fancy.pattern.factory.model.ProductApple;
import com.fancy.pattern.factory.model.ProductSamsung;

public abstract class IAbstractFactory {

	abstract ProductApple createProductApple();
	
	abstract ProductSamsung createProductSamsung();
}
