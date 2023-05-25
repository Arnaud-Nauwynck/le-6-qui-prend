module fr.an.game.le6quiprend.common {

    requires retrofit2;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires retrofit2.converter.jackson;
    requires com.google.common;
    // requires java.beans;

    requires static lombok;

    exports fr.an.game.le6quiprend.common;
    exports fr.an.game.le6quiprend.common.restapi;
    exports fr.an.game.le6quiprend.common.restapi.dto;
}