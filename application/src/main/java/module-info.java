module application {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires kotlinx.coroutines.core.jvm;
    requires shared;
    requires javafx.web;
    requires flexmark;
    requires flexmark.util.ast;
    requires flexmark.util.data;
    requires flexmark.ext.tables;
    requires flexmark.ext.gfm.strikethrough;
    requires flexmark.util.misc;
    requires flexmark.ext.gitlab;
    requires flexmark.ext.emoji;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;
    exports net.codebot.application;
}