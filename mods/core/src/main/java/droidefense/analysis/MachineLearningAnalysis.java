package droidefense.analysis;

import apkr.external.modules.helpers.log4j.Log;
import apkr.external.modules.helpers.log4j.LoggerType;
import droidefense.analysis.base.AbstractAndroidAnalysis;
import droidefense.handler.WekaResultsHandler;

/**
 * Created by sergio on 25/3/16.
 */
public final class MachineLearningAnalysis extends AbstractAndroidAnalysis {

    @Override
    protected boolean analyze() {
        Log.write(LoggerType.TRACE, "\n\n --- Running Droidefense ML analysis ---\n\n");
        //run pscout apimodel
        WekaResultsHandler handler = new WekaResultsHandler();
        handler.setApk(apkFile);
        handler.setProject(currentProject);
        //TODO in-memory weka scan. do not generate features file
        executionSuccessful = handler.doTheJob();
        return executionSuccessful;
    }

    @Override
    public String getName() {
        return "Machine learning analysis";
    }
}