package com.snobot.lib.autonomous;

import org.junit.Test;

import com.snobot2017.test.utilities.BaseTest;

public class TestAutonCrawler extends BaseTest
{

    @Test
    public void testCrawler()
    {
        SnobotAutonCrawler crawler = new SnobotAutonCrawler("");

        crawler.loadAutonFiles("test_inputs");
    }

    @Test
    public void testCrawlerDoesNotExistDirectory()
    {
        SnobotAutonCrawler crawler = new SnobotAutonCrawler("");

        crawler.loadAutonFiles("does_not_exist");
    }
}
