package cn.sunxyz.webcrawler.parser;

import java.util.Set;

import cn.sunxyz.webcrawler.download.Page;

public interface LinkParser {

	Set<String> getLinks(Page page);
}
