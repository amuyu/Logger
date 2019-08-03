package com.github.amuyu.logger.lint;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

public class WrongUsageDetector extends Detector implements Detector.UastScanner {

    static Issue[] getIssues() {
        return new Issue[]{ };
    }
}
