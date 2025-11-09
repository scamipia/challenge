package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.ShippingOption;
import java.util.List;

public interface ShippingOptionRepository {
    List<ShippingOption> findByItemIdAndZipcode(String itemId, String zipcode);
}


