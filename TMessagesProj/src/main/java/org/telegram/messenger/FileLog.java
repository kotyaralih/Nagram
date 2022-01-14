/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.messenger;

import java.io.File;
import java.io.OutputStreamWriter;
import org.telegram.messenger.time.FastDateFormat;
import org.telegram.messenger.video.MediaCodecVideoConvertor;
import top.qwq2333.nullgram.utils.LogUtilsKt;

public class FileLog {

    private final OutputStreamWriter streamWriter = null;
    private final FastDateFormat dateFormat = null;
    private final DispatchQueue logQueue = null;
    private final File currentFile = null;
    private File networkFile = null;
    private File tonlibFile = null;
    private boolean initied;

    private final static String tag = "tmessages";

    private static volatile FileLog Instance = null;

    public static FileLog getInstance() {
        FileLog localInstance = Instance;
        if (localInstance == null) {
            synchronized (FileLog.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new FileLog();
                }
            }
        }
        return localInstance;
    }

    public FileLog() {
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        init();
    }

    public void init() {
        if (initied) {
            return;
        }
        initied = true;
    }

    public static void ensureInitied() {
        getInstance().init();
    }

    public static String getNetworkLogPath() {
        if (!BuildVars.LOGS_ENABLED) {
            return "";
        }
        try {
            File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
            if (sdCard == null) {
                return "";
            }
            File dir = new File(sdCard.getAbsolutePath() + "/logs");
            dir.mkdirs();
            getInstance().networkFile = new File(dir, getInstance().dateFormat.format(System.currentTimeMillis()) + "_net.txt");
            return getInstance().networkFile.getAbsolutePath();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTonlibLogPath() {
        if (!BuildVars.LOGS_ENABLED) {
            return "";
        }
        try {
            File sdCard = ApplicationLoader.applicationContext.getExternalFilesDir(null);
            if (sdCard == null) {
                return "";
            }
            File dir = new File(sdCard.getAbsolutePath() + "/logs");
            dir.mkdirs();
            getInstance().tonlibFile = new File(dir, getInstance().dateFormat.format(System.currentTimeMillis()) + "_tonlib.txt");
            return getInstance().tonlibFile.getAbsolutePath();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @deprecated use {@link LogUtilsKt#e(String, Throwable)} instead
     */
    @Deprecated
    public static void e(final String message, final Throwable exception) {
        LogUtilsKt.e(message, exception);
    }

    /**
     * @deprecated use {@link LogUtilsKt#e(String msg)} instead
     */
    @Deprecated
    public static void e(final String message) {
        LogUtilsKt.e(message);
    }

    /**
     * @deprecated use {@link LogUtilsKt#e(String, Throwable)} instead
     */
    public static void e(final Throwable e) {
        e(e, true);
    }

    /**
     * @deprecated use {@link LogUtilsKt#e(String, Throwable)} instead
     */
    @Deprecated
    public static void e(final Throwable e, boolean logToAppCenter) {
        if (needSent(e) && logToAppCenter) {
            AndroidUtilities.appCenterLog(e);
        }
        LogUtilsKt.e("", e);
    }

    private static boolean needSent(Throwable e) {
        return !(e instanceof InterruptedException)
            && !(e instanceof MediaCodecVideoConvertor.ConversionCanceledException);
    }

    /**
     * @deprecated use {@link #e(String)} instead
     * @param message
     */
    @Deprecated
    public static void d(final String message) {
        LogUtilsKt.d(message);
    }

    /**
     * @deprecated use {@link LogUtilsKt#w(String msg)} )} instead
     */
    @Deprecated
    public static void w(final String message) {
        LogUtilsKt.w(message);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static void cleanupLogs() {
    }
}
