package core.basesyntax;

import core.basesyntax.service.FileReaderImpl;
import core.basesyntax.service.FileWriter;
import core.basesyntax.service.FileWriterImpl;
import core.basesyntax.service.RecordTransformer;
import core.basesyntax.service.RecordTransformerImpl;
import core.basesyntax.service.ReportCreator;
import core.basesyntax.service.ReportCreatorImpl;
import core.basesyntax.strategy.ActivityStrategy;
import core.basesyntax.strategy.ActivityStrategyImpl;
import core.basesyntax.strategy.activity.ActivityHandler;
import core.basesyntax.strategy.activity.ActivityHandlerAddImpl;
import core.basesyntax.strategy.activity.ActivityHandlerSubstractionImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String FILE_NAME_FROM =
            "src/main/java/core/basesyntax/resources/inputData.csv";
    private static final String FILE_NAME_TO =
            "src/main/java/core/basesyntax/resources/reportData.csv";
    private static final String BALANCE = "b";
    private static final String SUPPLY = "s";
    private static final String PURCHASE = "p";
    private static final String RETURN = "r";
    private static final int TITLE_INDEX = 0;

    public static void main(String[] args) {
        List<String> fileData = new FileReaderImpl().read(FILE_NAME_FROM);
        fileData.remove(TITLE_INDEX);
        RecordTransformer recordTransformer = new RecordTransformerImpl();
        recordTransformer.transform(fileData);
        Map<String, ActivityHandler> activityHandlerMap = new HashMap<>();
        activityHandlerMap.put(BALANCE, new ActivityHandlerAddImpl());
        activityHandlerMap.put(SUPPLY, new ActivityHandlerAddImpl());
        activityHandlerMap.put(PURCHASE, new ActivityHandlerSubstractionImpl());
        activityHandlerMap.put(RETURN, new ActivityHandlerAddImpl());
        ActivityStrategy activityStrategy = new ActivityStrategyImpl(activityHandlerMap);
        ReportCreator reportCreator = new ReportCreatorImpl(activityStrategy);
        List<String> newReport = reportCreator.createReport();
        FileWriter fileWriter = new FileWriterImpl();
        fileWriter.write(newReport, FILE_NAME_TO);
    }
}