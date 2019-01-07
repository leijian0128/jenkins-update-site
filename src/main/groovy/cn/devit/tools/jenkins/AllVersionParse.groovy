package cn.devit.tools.jenkins

import com.google.common.io.Closer
import com.google.common.io.Resources

/**
 *
 * <p>
 *
 * @author lxb
 */
class AllVersionParse {

    File pwd;

    void parseAndDownload(File downloadFolder, URL index) {
        pwd = downloadFolder;

        ParseOneVersion job = new ParseOneVersion();

        List<IndexFileParser.IndexItem> list = new IndexFileParser().parseIndexFile(index);
        list.each { it ->
            String name = it.name;
            if (name =~ /\d+\.\d+/) {
                //this is common in support version.
                println "Entering ${name}"
                File dir = mkdir(name)
                job.parseAndSave(dir, new URL(index, name));
            } else if (name == 'current/') {
                //this is latest version.
                println "Entering ${name}"
                File dir = mkdir(name)
                job.parseAndSave(dir, new URL(index, name));
            } else if (name.startsWith('stable-')) {
                //a stable version support.
                println "Entering ${name}"
                File dir = mkdir(name)
                job.parseAndSave(dir, new URL(index, name));
            } else if (name == 'stable/') {
                //latest stable
                println "Entering ${name}"
                File dir = mkdir(name)
                job.parseAndSave(dir, new URL(index, name));
            } else if (name == 'update-center.actual.json') {

            } else if (name == 'update-center.json') {
                //skip.
            } else if (name == 'update-center.json.html') {
                //skip.
            } else if (name == 'updates/') {
                println "Entering ${name}"
                // go into
                File updatesDir = mkdir(name);
                folderUpdateIsToolsInstallIndex(updatesDir, new URL(index, name));

            } else {
                println "Skip unknown: ${name}"
            }
        }

    }

    public File mkdir(String name) {
        File dir = new File(pwd, name);
        dir.mkdir();
        return dir
    }

    void folderUpdateIsToolsInstallIndex(File pwd, URL index) {
        def list = new IndexFileParser().parseIndexFile(index);
        Closer closer = Closer.create();
        try {
            list.each { it ->
                String name = it.name;
                try {
                    if (name.endsWith(".json")) {
                        //download to temp file then move to desire location.
                        File file = File.createTempFile("wget", "download")
                        println "Downloading ${name}"
                        Resources.copy(new URL(index, name),
                                closer.register(new FileOutputStream(file)))
                        file.renameTo(new File(pwd, name));
                    } else {
                        println "skip ${name}"
                    }
                } catch (IOException e) {
                    //continue;
                }
            }
        } finally {
            closer.close();
        }
    }
}