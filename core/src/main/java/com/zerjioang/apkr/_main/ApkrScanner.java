package com.zerjioang.apkr._main;

import com.zerjioang.apkr.temp.ApkrIntelligence;
import com.zerjioang.apkr.v1.common.analysis.base.AbstractAndroidAnalysis;
import com.zerjioang.apkr.v1.common.analysis.base.AnalysisFactory;
import com.zerjioang.apkr.v1.common.datamodel.base.APKFile;
import com.zerjioang.apkr.v1.common.datamodel.base.ApkrProject;
import com.zerjioang.apkr.v1.common.handlers.FileIOHandler;
import com.zerjioang.apkr.v2.exception.InvalidScanParametersException;
import com.zerjioang.apkr.v2.helpers.config.ApkrConstants;
import com.zerjioang.apkr.v2.helpers.log4j.Log;
import com.zerjioang.apkr.v2.helpers.log4j.LoggerType;

import java.io.File;
import java.security.cert.CertificateException;

/**
 * Created by sergio on 3/9/16.
 */
public class ApkrScanner {

    public static final byte LOAD_VARIABLES = 0x0;
    private static boolean init = false;
    private ApkrProject project;

    public ApkrScanner(byte idx) {
        switch (idx) {
            case LOAD_VARIABLES:
                loadVariables();
                break;
        }
    }

    public ApkrScanner(String[] args) throws InvalidScanParametersException {
        //security check
        if (args != null && args.length == 1) {
            initScan(args);
        } else {
            throw new InvalidScanParametersException("Received parameters are not valid to launch the scan", args);
        }
    }

    public static void main(String[] args) throws CertificateException, InvalidScanParametersException {
        new ApkrScanner(new String[]{FileIOHandler.getBaseDirFile().getParentFile().getAbsolutePath() + File.separator + "temp/sms.demo.apk"});
        new ApkrScanner(new String[]{FileIOHandler.getBaseDirFile().getParentFile().getAbsolutePath() + File.separator + "temp/sms.demo.with.apk"});
    }

    public void stop() {
        //save report .json to file
        Log.write(LoggerType.TRACE, "Saving report file...");
        project.finish();
        Log.write(LoggerType.TRACE, "apkr scan finished");
    }

    private void initScan(String[] args) {
        //execute only once
        loadVariables();
        //read dex file from foldex x file y
        APKFile apk;

        Log.write(LoggerType.TRACE, "Reading .apk from local file");
        apk = new APKFile(args[0], APKFile.APKTOOL);

        Log.write(LoggerType.TRACE, "Building project");
        project = new ApkrProject(apk);

        Log.write(LoggerType.TRACE, "Running ApkrScan");

        Log.write(LoggerType.TRACE, "Project ID:\t" + project.getProjectId());

        AbstractAndroidAnalysis analyzer;
        analyzer = AnalysisFactory.getAnalyzer(AnalysisFactory.GENERAL);

        //Start analysis
        project.analyze(analyzer);
        //stop scan
        this.stop();
    }

    private void loadVariables() {
        if (!init) {
            //init data structs
            ApkrConstants.init();
            Log.write(LoggerType.TRACE, "Loading apkr data structs...");
            //create singleton instance of AtomIntelligence
            ApkrIntelligence.getInstance();
            Log.write(LoggerType.TRACE, "Data loaded!!");
            init = true;
        }
    }
}