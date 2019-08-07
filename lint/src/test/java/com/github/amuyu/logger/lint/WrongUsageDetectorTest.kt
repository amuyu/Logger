package com.github.amuyu.logger.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongUsageDetectorTest {


    @Test
    fun testUsingAndroidLog() {
        lint()
                .files(
                        java("""
                |package foo;
                |import android.util.Log;
                |public class Example {
                |  public void log() {
                |    Log.d("TAG", "msg");
                |  }
                |}""".trimMargin())
                )
                .issues(WrongUsageDetector.ISSUE_LOG)
                .run()
                .expect("""
            |src/foo/Example.java:5: Warning: Using 'Log' instead of 'Logger' [LogNotLogger]
            |    Log.d("TAG", "msg");
            |    ~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
                .expectFixDiffs("""
            |Fix for src/foo/Example.java line 4: Replace with Logger.d("msg"):
            |@@ -5 +5
            |-     Log.d("TAG", "msg");
            |+     Logger.d("msg");
            |""".trimMargin())

        lint()
                .files(
                        java("""
                |package foo;
                |import android.util.Log;
                |public class Example {
                |  public void log() {
                |    Log.d("TAG", "msg", new Exception());
                |  }
                |}""".trimMargin())
                )
                .issues(WrongUsageDetector.ISSUE_LOG)
                .run()
                .expect("""
            |src/foo/Example.java:5: Warning: Using 'Log' instead of 'Logger' [LogNotLogger]
            |    Log.d("TAG", "msg", new Exception());
            |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
                .expectFixDiffs("""
            |Fix for src/foo/Example.java line 4: Replace with Logger.d(new Exception(), "msg"):
            |@@ -5 +5
            |-     Log.d("TAG", "msg", new Exception());
            |+     Logger.d(new Exception(), "msg");
            |""".trimMargin())
    }
}
