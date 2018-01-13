package com.example.aswin.myapplication.listener_interface;

import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.util.List;

/**
 * Created by ASWIN on 1/13/2018.
 */

public interface FileImportListener {
    void OnImportFinished(List<MoneyDonor> donors);
    void OnIOException();
    void OnNullPointerException();
    void OnIllegalStateException();
}
