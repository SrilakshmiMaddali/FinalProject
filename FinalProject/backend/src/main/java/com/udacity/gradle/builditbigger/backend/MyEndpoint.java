package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

import sm.com.javajoker.Joker;
import sm.com.javajoker.model.JokeData;
import sm.com.javajoker.repo.ichdj.IchdjDownloadSeed;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    private final Joker source = new Joker();
    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "getMyJoke")
    public JokeBean getMyJoke() {
        JokeData data = source.nextJoke(new IchdjDownloadSeed());

        JokeBean response = new JokeBean();
        response.setJokeBody(data.jokeBody);
        response.setJokeFollowUp(data.optFollowup);
        response.setJokeStatus(data.statusCode);

        return response;
    }
}
