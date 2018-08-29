package sm.com.javajoker;

import sm.com.javajoker.model.JokeData;
import sm.com.javajoker.repo.DownloadSeed;

public class Joker {
    private DownloadSeed downloadSeed;

    public Joker() {

    }

    public Joker(DownloadSeed downloadSeed) {
        this.downloadSeed = downloadSeed;
    }

    /**
     * Fetch the next joke using the passed in download seed.
     *
     * @param downloadSeed The seed containing download information.
     * @return A JokeData object.
     */
    public JokeData nextJoke(DownloadSeed downloadSeed) {
        this.downloadSeed = downloadSeed;
        return nextJoke();
    }

    /**
     * Fetch the next joke using a provided seed
     *
     * @return A JokeData object.
     */
    public JokeData nextJoke() {
        //If download seed is null return JokeData object with error code and message.
        if (downloadSeed == null)
            return new JokeData("ERROR: Seed not initialized", null, JokeData.STATUS_ERROR);

        return downloadSeed.getData();
    }
}
