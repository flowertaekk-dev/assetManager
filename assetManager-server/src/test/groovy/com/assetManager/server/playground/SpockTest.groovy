package com.assetManager.server.playground

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SpockTest extends Specification {

    def "first spock test"() {
        given:
        List<Integer> list = new ArrayList<>();

        when:
        list.add(2);

        then:
        2 == list.get(0);
    }


}
