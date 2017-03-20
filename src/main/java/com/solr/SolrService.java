package com.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrService {

	private static final String baseURL = "http://localhost:8080/solr";
	private static final SolrClient client = new HttpSolrClient(baseURL);
	
	public static Random rand = new Random(47);
	public static String[] authors = { "张三", "李四", "王五", "赵六", "张飞", "刘备",
			"关云长" };
	public static String[] links = {
			"http://repository.sonatype.org/content/sites/forge-sites/m2e/",
			"http://news.ifeng.com/a/20140818/41626965_0.shtml",
			"http://news.ifeng.com/a/20140819/41631363_0.shtml?wratingModule_1_9_1",
			"http://news.ifeng.com/topic/19382/",
			"http://news.ifeng.com/topic/19644/" };

	public static String genAuthors() {
		List<String> list = Arrays.asList(authors).subList(0, rand.nextInt(7));
		String str = "";
		for (String tmp : list) {
			str += " " + tmp;
		}
		return str;
	}

	public static List<String> genLinks() {
		return Arrays.asList(links).subList(0, rand.nextInt(5));
	}
	
	public static void main(String[] args) throws SolrServerException, IOException {
		// 通过浏览器查看结果
		// 要保证bean中各属性的名称在conf/schema.xml中存在，如果查询，要保存被索引
		// http://172.168.63.233:8983/solr/collection1/select?q=description%3A%E6%94%B9%E9%9D%A9&wt=json&indent=true
//		delBeans();
//		AddBeans();
		queryBeans();
	}

	

	public static void AddBeans() {
		String[] words = { "中央全面深化改革领导小组", "第四次会议", "审议了国企薪酬制度改革", "考试招生制度改革",
				"传统媒体与新媒体融合等", "相关内容文件", "习近平强调要", "逐步规范国有企业收入分配秩序",
				"实现薪酬水平适当", "结构合理、管理规范、监督有效", "对不合理的偏高", "过高收入进行调整",
				"深化考试招生制度改革", "总的目标是形成分类考试", "综合评价", "多元录取的考试招生模式", "健全促进公平",
				"科学选才", "监督有力的体制机制", "着力打造一批形态多样", "手段先进", "具有竞争力的新型主流媒体",
				"建成几家拥有强大实力和传播力", "公信力", "影响力的新型媒体集团" };

		long start = System.currentTimeMillis();
		Collection<NewsBean> docs = new ArrayList<NewsBean>();
		for (int i = 1; i < 300; i++) {
			NewsBean news = new NewsBean();
			news.setId("id" + i);
			news.setName("news" + i);
			news.setAuthor(genAuthors());
			news.setDescription(words[i % 21]);
			news.setRelatedLinks(genLinks());
			docs.add(news);
		}
		try {
			client.addBeans(docs);
			client.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("time elapsed(ms):"
				+ (System.currentTimeMillis() - start));
	}

	public static void delBeans() {
		long start = System.currentTimeMillis();
		try {
			List<String> ids = new ArrayList<String>();
			for (int i = 1; i < 300; i++) {
				ids.add("id" + i);
			}
			client.deleteById(ids);
			client.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("time elapsed(ms):"
				+ (System.currentTimeMillis() - start));
	}
	
	
	public static void queryBeans() throws SolrServerException, IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("description:改革");
		query.setStart(0);
		query.setRows(2);
		query.setFacet(true);
		query.addFacetField("author_s");

		QueryResponse response = client.query(query);
		// 搜索得到的结果数
		System.out.println("Find:" + response.getResults().getNumFound());
		// 输出结果
		int iRow = 1;
		
		List<NewsBean> beanList=response.getBeans(NewsBean.class);
		for(NewsBean news:beanList){
			System.out.println(news.getId());
		}

		for (SolrDocument doc : response.getResults()) {
			System.out.println("----------" + iRow + "------------");
			System.out.println("id: " + doc.getFieldValue("id").toString());
			System.out.println("name: " + doc.getFieldValue("name").toString());
			iRow++;
		}
		for (FacetField ff : response.getFacetFields()) {
			System.out.println(ff.getName() + "," + ff.getValueCount() + ","
					+ ff.getValues());
		}
	}
}
