package com.github.tobato.fastdfs.context.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * fastdfs Banner图制作
 *
 * @author qiuzulin
 * @date 2020-09-29 13:34
 */
public class FastdfsBannerApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Log logger = LogFactory.getLog(FastdfsBannerApplicationListener.class);

    private static Banner.Mode BANNER_MODE;

    public FastdfsBannerApplicationListener() {
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        if (BANNER_MODE != Banner.Mode.OFF) {
            String bannerText = this.buildBannerText();
            if (BANNER_MODE == Banner.Mode.CONSOLE) {
                System.out.print(bannerText);
            } else if (BANNER_MODE == Banner.Mode.LOG) {
                logger.info(bannerText);
            }

        }
    }

    private String buildBannerText() {
        return AnsiOutput.toString(FastdfsLogo.LINE_SEPARATOR,AnsiColor.DEFAULT,FastdfsLogo.BANER,AnsiColor.GREEN, FastdfsLogo.FASTDFS_STARTER,AnsiColor.DEFAULT,AnsiStyle.FAINT,FastdfsLogo.VERSION,FastdfsLogo.LINE_SEPARATOR);
    }


    static {
        BANNER_MODE = Banner.Mode.CONSOLE;
    }

    public static Banner.Mode getBannerMode() {
        return BANNER_MODE;
    }

    public static void setBannerMode(Banner.Mode bannerMode) {
        BANNER_MODE = bannerMode;
    }
}
