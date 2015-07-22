package scripts;

import javafx.application.Platform;
import main.Main;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

public class DownloadScript {

    public static double totalSize;

    public static class DownloadCountingOutputStream extends CountingOutputStream {

        private ActionListener listener = null;

        public DownloadCountingOutputStream(OutputStream out) {
            super(out);
        }

        public void setListener(ActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected void afterWrite(int n) throws IOException {
            super.afterWrite(n);
            if (listener != null) {
                listener.actionPerformed(new ActionEvent(this, 0, null));
            }
        }

    }

    private static class ProgressListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double downloaded = ((DownloadCountingOutputStream) e.getSource()).getByteCount();
                Main.mainController.detailsController.scriptsTabController.progressBar.setProgress((downloaded/totalSize));
                //System.out.println((downloaded/totalSize)*100);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void download(String url, String name) {
        try {
            URL dl = null;
            File fl = null;
            String x = null;
            OutputStream os = null;
            InputStream is = null;
            ProgressListener progressListener = new ProgressListener();
            try {
                if (!new File(Main.rootPath + "/resources/scripts/temp/").exists()) new File(Main.rootPath + "/resources/scripts/temp/").mkdir();
                fl = new File(Main.rootPath + "/resources/scripts/temp/" + name);
                dl = new URL(url);
                os = new FileOutputStream(fl);
                is = dl.openStream();

                DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
                dcount.setListener(progressListener);

                totalSize = Long.parseLong(dl.openConnection().getHeaderField("Content-Length"));
                IOUtils.copy(is, dcount);
                os.close();

                new ZipFile(fl).extractAll(Main.rootPath + "/resources/scripts/");
                fl.delete();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        Main.mainController.detailsController.scriptsTabController.fillTable();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
