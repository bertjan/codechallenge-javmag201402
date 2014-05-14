package nl.revolution.codechallenge;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordListProvider {

    private static final String WORD_LIST_URL = "http://bit.ly/alfabetsoep";
    private static final String WORD_LIST_FILE_NAME = "wordlist.txt";
    private static final String DROPBOX_DOWNLOAD_URL_PREFIX = "https://dl.dropboxusercontent.com";
    private static final String ENCODING = Charsets.UTF_8.name();
    private static final Logger LOG = LoggerFactory.getLogger(WordListProvider.class);

    private File getDataFile() {
        URL currentClassPath = this.getClass().getClassLoader().getResource("");
        if (currentClassPath == null) {
            return null;
        }
        return new File(currentClassPath.getPath(), WORD_LIST_FILE_NAME);
    }

    private List<String> readWordFileFromDisk() {
        try {
            if (!getDataFile().exists()) {
                return null;
            }
            return FileUtils.readLines(getDataFile(), ENCODING);
        } catch (IOException e) {
            LOG.error("Error while reading word list from disk", e);
            return null;
        }
    }

    private List<String> downloadWordList() {
        String dropboxHtml = performHttpGet(WORD_LIST_URL);
        String wordListDownloadLink = DROPBOX_DOWNLOAD_URL_PREFIX + StringUtils.substringBetween(dropboxHtml, DROPBOX_DOWNLOAD_URL_PREFIX, "\"");
        String wordList = performHttpGet(wordListDownloadLink);
        return Arrays.asList(wordList.split("\n"));
    }

    private String performHttpGet(String url) {
        try {
            return EntityUtils.toString(HttpClients.createDefault().execute(new HttpGet(url)).getEntity());
        } catch (IOException e) {
            LOG.error("Error while performing HTTP GET to url '" + url + "'", e);
            return null;
        }
    }

    private void writeWordListToDisk(List<String> words) {
        try {
            FileUtils.writeLines(getDataFile(), ENCODING, words);
        } catch (IOException e) {
            LOG.error("Error while writing word list to disk", e);
        }
    }

    public List<String> getWordList() {
        List<String> words = readWordFileFromDisk();
        if (words != null) {
            LOG.info("Read " + words.size() + " words from disk (" + getDataFile().getAbsolutePath() + ").");
            return words;
        }

        LOG.info("No word list found on disk. Downloading new list from " + WORD_LIST_URL + ".");
        words = downloadWordList();
        LOG.info("Downloaded list with " + words.size() + " words.");

        writeWordListToDisk(words);
        LOG.info("Wrote word list to disk (" + getDataFile().getAbsolutePath() + ").");

        return words;
    }

    public List<char[]> toCharArrayList(List<String> stringWordList) {
        return stringWordList.stream().map(String::toCharArray).collect(Collectors.toList());
    }

}
