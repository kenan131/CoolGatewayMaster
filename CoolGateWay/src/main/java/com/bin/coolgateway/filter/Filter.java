package com.bin.coolgateway.filter;

import com.bin.coolgateway.model.Context;

/**
 * @author: bin
 * @date: 2023/12/20 10:07
 **/

public interface Filter {

    void doFiler(Context context);

    default int getOrder(){
        return 9999;
    }
}
