package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistModel;
import com.ay_za.ataylar_technic.vm.FileResponseVM;

import java.io.IOException;

public interface MaintenanceChecklistPdfServiceImpl {

    FileResponseVM exportPdf(MaintenanceChecklistModel report) throws IOException;
}
