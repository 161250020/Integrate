package com.mwx.springboot.service;

import com.mwx.springboot.dao.DoubanDataService;
import com.mwx.springboot.entity.PageBean;
import com.mwx.springboot.entity.douban.Hotcomment;
import com.mwx.springboot.entity.douban.Movie;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DoubanServiceImpl implements  DoubanService{
    @Autowired
    DoubanDataService doubanDataService;

    @Override
    public PageBean findDouBanDataByConPage(int pageCode, int pageSize) {
        return doubanDataService.findDouBanDataByConPage(pageCode,pageSize);
    }

    @Override
    public Movie findDouBanDataByMovieName(String name) {
        //新xml文件的名称
        String src = doubanDataService.findDouBanDataByMovieName(name);
        //String src="20190517133453301.xml";
//        src = "douban/" + src;
        //复制数据到xml
        String dest = "xslts/douBanMovies.xml";
        String xslt = "xslts/xsltDouban.xsl";
        new DoubanServiceImpl().copyToXml(src, dest, xslt);

        return new DoubanServiceImpl().getMovie(src);
    }

    @Override
    public List<Movie> searchDouBanMovies(String searchInfo) {
        //新xml文件的名称
        String src = doubanDataService.searchDouBanMovies(searchInfo);
//        src = "douban/" + src;
        //复制数据到xml
        String dest = "xslts/douBanMovies.xml";
        String xslt = "xslts/xsltDouban.xsl";
        new DoubanServiceImpl().copyToXml(src, dest, xslt);

        return new DoubanServiceImpl().getMovies(src);
    }

    //转换豆瓣的数据到xml中
    private void copyToXml(String src, String dest, String xslt){
        File src2 = new File(src);
        File dest2 = new File(dest);
        File xslt2 = new File (xslt);

        Source srcSource = new StreamSource(src2);
        Result destResult = new StreamResult(dest2);
        Source xsltSource = new StreamSource(xslt2);

        try{
            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer trans = transFact.newTransformer(xsltSource);
            trans.transform(srcSource,destResult);
        }catch(TransformerConfigurationException e){
            e.printStackTrace();
        }catch(TransformerFactoryConfigurationError e){
            e.printStackTrace();
        }catch(TransformerException e){
            e.printStackTrace();
        }
    }

    //获得movie的返回值
    private Movie getMovie(String src){
        Movie ret = new Movie();
        try {
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //读取文件 转换成Document
            Document document = null;
            document = reader.read(new File(src));
            //获取根节点元素对象
            Element root = document.getRootElement();

            //同时迭代当前节点下面的所有子节点
            List<Element> movies = root.elements();

            ret.setId(Integer.parseInt(movies.get(0).elementText("id")));
            ret.setTitle(movies.get(0).elementText("title"));
            ret.setDirectors(movies.get(0).elementText("directors"));
            ret.setRate(movies.get(0).elementText("rate"));
            ret.setCasts(movies.get(0).elementText("casts"));
            ret.setType(movies.get(0).elementText("type"));
            ret.setNation(movies.get(0).elementText("nation"));
            ret.setLanguage(movies.get(0).elementText("language"));
            ret.setDate(movies.get(0).elementText("date"));
            ret.setTime(movies.get(0).elementText("time"));
            ret.setPeopleNumber(movies.get(0).elementText("peopleNumber"));
            ret.setIntroduction(movies.get(0).elementText("introduction"));

            List<Hotcomment> coms = new ArrayList<>();
            List<Element> hotComments = movies.get(0).element("hotComments").elements();
            for(Element e : hotComments){
                Hotcomment h = new Hotcomment();
                h.setMovie_id(Integer.parseInt(movies.get(0).elementText("id")));
                h.setHotCommentAuthor(e.elementText("hotCommentAuthor"));
                h.setHotCommentDate(e.elementText("hotCommentDate"));
                h.setHotCommentContent(e.elementText("hotCommentContent"));
                coms.add(h);
            }
            ret.setHotcommentList(coms);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return ret;
    }

    //获得movies的返回值
    private List<Movie> getMovies(String src){
        List<Movie> rets = new ArrayList<>();
        try {
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //读取文件 转换成Document
            Document document = null;
            document = reader.read(new File(src));
            //获取根节点元素对象
            Element root = document.getRootElement();

            //同时迭代当前节点下面的所有子节点
            List<Element> movies = root.elements();
            for(int i = 0; i < movies.size(); i++){
                Movie ret = new Movie();
                ret.setId(Integer.parseInt(movies.get(i).elementText("id")));
                ret.setTitle(movies.get(i).elementText("title"));
                ret.setDirectors(movies.get(i).elementText("directors"));
                ret.setRate(movies.get(i).elementText("rate"));
                ret.setCasts(movies.get(i).elementText("casts"));
                ret.setType(movies.get(i).elementText("type"));
                ret.setNation(movies.get(i).elementText("nation"));
                ret.setLanguage(movies.get(i).elementText("language"));
                ret.setDate(movies.get(i).elementText("date"));
                ret.setTime(movies.get(i).elementText("time"));
                ret.setPeopleNumber(movies.get(i).elementText("peopleNumber"));
                ret.setIntroduction(movies.get(i).elementText("introduction"));

                List<Hotcomment> coms = new ArrayList<>();
                List<Element> hotComments = movies.get(i).element("hotComments").elements();
                for(Element e : hotComments){
                    Hotcomment h = new Hotcomment();
                    h.setMovie_id(Integer.parseInt(movies.get(i).elementText("id")));
                    h.setHotCommentAuthor(e.elementText("hotCommentAuthor"));
                    h.setHotCommentDate(e.elementText("hotCommentDate"));
                    h.setHotCommentContent(e.elementText("hotCommentContent"));
                    coms.add(h);
                }
                ret.setHotcommentList(coms);

                rets.add(ret);
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return rets;
    }
}
