package com.study.spider;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.chinawiserv.credit.common.utils.time.DateUtils;
//import com.chinawiserv.credit.crawler.captcha.CaptchaUtils;
//import com.chinawiserv.credit.crawler.conf.ActionCode;
//import com.chinawiserv.credit.crawler.model.ActionResult;
//import com.chinawiserv.credit.crawler.model.AddrResult;
//import com.chinawiserv.credit.crawler.model.OrderInfo;
//import com.chinawiserv.credit.crawler.poster.base.AbstractPoster;
//import com.chinawiserv.credit.crawler.utils.HttpUtil;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.log4j.Logger;
//import org.htmlcleaner.CleanerProperties;
//import org.htmlcleaner.DomSerializer;
//import org.htmlcleaner.HtmlCleaner;
//import org.htmlcleaner.TagNode;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathFactory;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 电商数据爬虫
// * <p>
// * Created by sungang on 2016/8/31.
// */
//public class JDCrawler extends AbstractPoster {
//
//    private static Logger log = Logger.getLogger(JDCrawler.class);
//
//
//    protected Map<String, String> goods_name_map = Maps.newConcurrentMap();
//
//    public JDCrawler() {
//
//    }
//
//
//    public JDCrawler(String account, String password) {
//        params.put("username", account);
//        params.put("password", password);
//    }
//
//    /**
//     *
//     */
//    public ActionResult login() throws Exception {
//        log.info("开始登录京东网站");
//        ActionResult actionResult = new ActionResult();
//        String login_host = "passport.jd.com";
//        //************************第一步访问登录页面***************************
//        log.debug("访问登录页面");
//
//        String login_url_page = "https://passport.jd.com/uc/login";
//        HttpGet httpGet1 = HttpUtil.simpleHttpGet(login_url_page);
//        httpGet1.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        httpGet1.setHeader("Accept-Encoding", "gzip, deflate, br");
//        httpGet1.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//        httpGet1.setHeader("Connection", "keep-alive");
//        httpGet1.setHeader("Host", login_host);
//        httpGet1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
//        log.debug("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.");
//        String response_1 = httpClient.execute(httpGet1, responseHandler, localContext).toString();
//
//        htmlCleaner(response_1);
//
//        //验证码第一种情况 打开登录页面直接出现验证码
//        String vcode = super.getConfigValue(response_1, "id=\"JD_Verification1\" class=\"(.*?)\"", "");
//
//        //判断是否出现登录验证码
//        if (StringUtils.isNotBlank(vcode)) {
//            log.info("打开登录页面,登录验证码出现了");
//            String uid = params.get("uuid");
//            String get_code_url = "https://authcode.jd.com/verify/image?a=1&acid=" + uid + "&uid=" + uid + "&yys=" + System.currentTimeMillis();
////            log.info("短信码URL:" + get_code_url);
//            String imageName = "captcha_" + System.currentTimeMillis() + ".jpg";    //任务id + 时间戳
//            actionResult = CaptchaUtils.handleCaptcha(httpClient, responseHandler, localContext, get_code_url, login_url_page, "1004", imageName);
//            if (ActionCode.CLOUD_CAPTCHA_ERROR == actionResult.getCode()) {
//                log.debug("================打码失败!=================");
//                return actionResult;
//
//            }
//            String code = actionResult.getMessage();
//            log.info("云打码结果:" + code);
//            params.put("authcode", code);
//        }
//
//        //登录URL
//        String login_url = "https://passport.jd.com/uc/loginService?uuid=" + params.get("uuid") + "&&r=" + Math.random() + "&version=2015";
//        HttpPost httpPost_login = HttpUtil.httpPost(login_url, login_host, login_url_page);
//        httpPost_login.setHeader("Accept", "text/plain, */*; q=0.01");
//        httpPost_login.setHeader("Accept-Encoding", "gzip, deflate, br");
//        httpPost_login.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//        httpPost_login.setHeader("Connection", "keep-alive");
////            httpPost_login.setHeader("Cookie",params.get("login_page_cookie"));
//        httpPost_login.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
//        httpPost_login.setHeader("X-Requested-With", "XMLHttpRequest");
//        List<NameValuePair> login_nvps = new ArrayList<NameValuePair>();
//        for (String key : params.keySet()) {
//            if ("username".equals(key) || "password".equals(key) || "eid".equals(key) || "fp".equals(key)) {
//                continue;
//            }
//            login_nvps.add(new BasicNameValuePair(key, params.get(key)));
//        }
//        login_nvps.add(new BasicNameValuePair("loginname", params.get("username")));
//        login_nvps.add(new BasicNameValuePair("loginpwd", params.get("password")));
//        login_nvps.add(new BasicNameValuePair("nloginpwd", params.get("password")));
//        login_nvps.add(new BasicNameValuePair("authcode", params.get("authcode")));
//        login_nvps.add(new BasicNameValuePair("eid", ""));
//        login_nvps.add(new BasicNameValuePair("fp", ""));
//
//
//        httpPost_login.setEntity(new UrlEncodedFormEntity(login_nvps, "UTF-8"));
//        String login_response = httpClient.execute(httpPost_login, responseHandler, localContext).toString();
//        //({"success":"http://www.jd.com"})
//        login_response = login_response.substring(1, (login_response.length() - 1));
////        login_res = JSONObject.fromObject(login_response);
//        JSONObject login_res = JSONObject.parseObject(login_response);
//        log.info("京东登录返回结果:" + JSON.toJSONString(login_response));
//
//        //验证码出现了
//        String emptyAuthcode = "验证码出现";
//        if (login_res.containsKey("emptyAuthcode")) {
//            emptyAuthcode = login_res.getString("emptyAuthcode");
//        } else {
//            //出现短信验证码
//            if (login_res.containsKey("venture")) {
//                log.info("京东登录出现短信验证码");
//                actionResult = moblieCodeProcess(login_res, login_url_page);
//                return actionResult;
//            }
//        }
//        //验证码第二种情况 打开登录页面没有出现，点击登录时出现，此时参数_t 会变化，所以需要从新加载登录页面 从新获取相关参数
//        if ("".equals(emptyAuthcode)) {
//            log.debug("=============================点击登录时,登录验证码出现了===========================");
//            String uid = params.get("uuid");
//            String get_code_url = "https://authcode.jd.com/verify/image?a=1&acid=" + uid + "&uid=" + uid + "&yys=" + System.currentTimeMillis();
//            String imageName = "captcha_" + System.currentTimeMillis() + ".jpg";    //任务id + 时间戳
//            actionResult = CaptchaUtils.handleCaptcha(httpClient, responseHandler, localContext, get_code_url, login_url_page, "1004", imageName);
//            if (ActionCode.CLOUD_CAPTCHA_ERROR == actionResult.getCode()) {
//                log.debug("京东登录打码失败!");
//                return actionResult;
//            }
//            String code = actionResult.getMessage();
//            params.put("authcode", code);
//            response_1 = httpClient.execute(httpGet1, responseHandler, localContext).toString();
//            htmlCleaner(response_1);
//            //第二次登陆
//            login_url = "https://passport.jd.com/uc/loginService?uuid=" + params.get("uuid") + "&&r=" + Math.random() + "&version=2015";
//            httpPost_login = HttpUtil.httpPost(login_url, login_host, login_url_page);
//            login_nvps.clear();
//            login_nvps.add(new BasicNameValuePair("authcode", params.get("authcode")));
//            for (String key : params.keySet()) {
//                if ("username".equals(key) || "password".equals(key) || "eid".equals(key) || "fp".equals(key)) {
//                    continue;
//                }
//                login_nvps.add(new BasicNameValuePair(key, params.get(key)));
//            }
//            login_nvps.add(new BasicNameValuePair("loginname", params.get("username")));
//            login_nvps.add(new BasicNameValuePair("loginpwd", params.get("password")));
//            login_nvps.add(new BasicNameValuePair("nloginpwd", params.get("password")));
//            login_nvps.add(new BasicNameValuePair("eid", ""));
//            login_nvps.add(new BasicNameValuePair("fp", ""));
//            //删除原来的_t
//            httpPost_login.setEntity(new UrlEncodedFormEntity(login_nvps, "UTF-8"));
//            login_response = httpClient.execute(httpPost_login, responseHandler, localContext).toString();
//            login_response = login_response.substring(1, (login_response.length() - 1));
//            login_res = JSONObject.parseObject(login_response);
//
//            //验证码出现了
//            if (login_res.containsKey("emptyAuthcode")) {
//                emptyAuthcode = login_res.getString("emptyAuthcode");
//            } else {
//                if (login_res.containsKey("venture")) {
//                    actionResult = moblieCodeProcess(login_res, login_url_page);
//                    return actionResult;
//                }
//            }
//        }
//        if (login_res.containsKey("emptyAuthcode")) {
//            log.debug("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.验证码错误!" + login_res.getString("emptyAuthcode"));
//            processCodeFail(actionResult);
//        } else if (login_res.containsKey("pwd")) {
//            log.debug("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.用户名密码出错!" + login_res.getString("pwd"));
//            processLoginFail(actionResult);
//        } else if (login_res.containsKey("username")) {
//            log.debug("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.登录失败!" + login_res.getString("username"));
//            processLoginFail(actionResult);
//        }
//        log.info("登录返回：" + login_res.toString());
//        if (login_res.containsKey("success")) {
//            log.debug("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.登录成功!");
//        }
//        return actionResult;
//    }
//
//
//    /**
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ActionResult step1() throws Exception {
//        log.info("获取京东信息第一步开始");
//        ActionResult actionResult = new ActionResult();
//        //登录京东
//        actionResult = login();
//
//        int login_code = actionResult.getCode();
//
//        log.info("京东登录返回CODE:" + login_code);
//        int count = 0;
//        while (ActionCode.CLOUD_CAPTCHA_ERROR == login_code && count++ < 2) {
//            actionResult = login();
//            login_code = actionResult.getCode();
//        }
//        return actionResult;
//    }
//
//    /**
//     * @param code
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ActionResult step2(String code) throws Exception {
//        log.info("获取京东信息第二步开始");
//        ActionResult actionResult = new ActionResult();
//        JSONObject data = new JSONObject();
//        //验证手机短信码是否正确
//        if (null != code) {
//            String sendMobileCode = params.get("sendMobileCode");
//            String safe_url = params.get("safe_url");
//            actionResult = checkMoblie(sendMobileCode, safe_url, code);
//        }
//        //验证码不正确
//        if (ActionCode.SMS_CODE_CHECK_ERROR == actionResult.getCode()) {
//            return actionResult;
//        }
//        //获取收获地址
//        getAddr(data);
//        getOrder(data);
//        actionResult.setMessage(JSON.toJSONString(data));
//        log.info("获取京东信息第二步结束");
//        return actionResult;
//    }
//
//
//    /**
//     * 获取订单
//     */
//    private void getOrder(JSONObject data) throws Exception{
//        log.info("获取京东订单信息开始");
//        List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
//        //获取今年內所有订单信息
//        String url_2 = "http://order.jd.com/center/list.action?search=0&d=2&s=1024";
//
////        StringBuffer orderIds = new StringBuffer();
//        List<String> orderIds = Lists.newArrayList();
//        List<String> productIds = Lists.newArrayList();
//        getOrderByTime(url_2, orderInfos,orderIds,productIds);
//
////        Calendar calendar = Calendar.getInstance();
////        int year = calendar.get(Calendar.YEAR)-1;
////        String url_3 = "http://order.jd.com/center/list.action?search=0&d="+year+"&s=1024";
////        getOrderByTime(url_3,orderInfos);
//
//        if (orderInfos.size() > 0){
//            //获取商品名称
//            getGoodsName(orderInfos,orderIds,productIds,url_2);
//        }
//        data.put("orders", orderInfos);
////        actionResult.setMessage(JSON.toJSONString(order));
//    }
//
//
//    private List<OrderInfo> getGoodsName(List<OrderInfo> orderInfos,List<String> orderIds,List<String> productIds,String referer) throws Exception{
//
//            //        http://order.jd.com/lazy/getOrderListCountJson.action?callback=jQuery9480183&_=1473315534901
////            String url2 = "http://order.jd.com/lazy/getOrderListCountJson.action?callback=jQuery9480183&_=" + System.currentTimeMillis();
////            HttpGet httpGet2  = HttpUtil.httpGet(url2);
////            httpGet2.setHeader("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
////            httpGet2.setHeader("Accept-Encoding", "gzip, deflate");
////            httpGet2.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
////            httpGet2.setHeader("Connection", "keep-alive");
////            httpGet2.setHeader("Host", "order.jd.com");
////            httpGet2.setHeader("Referer", referer);
////            httpGet2.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
////            httpGet2.setHeader("X-Requested-With", "XMLHttpRequest");
////            String httpGet_res2 = httpClient.execute(httpGet2, responseHandler, localContext).toString();
//
////            //首先获取商品名称
//            String goods_name_url = "http://order.jd.com/lazy/getOrderProductInfo.action";
//            HttpPost goods_name_post  = HttpUtil.httpPost(goods_name_url);
//            List<NameValuePair> goods_nvps = new ArrayList<NameValuePair>();
//            goods_name_post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//            goods_name_post.setHeader("Accept-Encoding", "gzip, deflate");
//            goods_name_post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//            goods_name_post.setHeader("Connection", "keep-alive");
//            goods_name_post.setHeader("Host", "order.jd.com");
//            goods_name_post.setHeader("Referer", referer);
//            goods_name_post.setHeader("Cookie", params.get("login_cookie"));
//            goods_name_post.setHeader("Content-Type", "application/x-www-form-urlencoded");
//            goods_name_post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
//            goods_name_post.setHeader("X-Requested-With", "XMLHttpRequest");
//
//
//            String orderId_str = listToString(orderIds);
//            String productId_str = listToString(productIds);
//            String orderSiteIds = "";
//            String  orderTypes = "";
//            String  orderWareTypes = "";
//            for(int i=0; i< orderIds.size(); i++){
//                orderSiteIds += "0,";
//                orderTypes += "0,";
//                orderWareTypes += "0,";
//            }
//            goods_nvps.add(new BasicNameValuePair("orderIds", orderId_str));
//            goods_nvps.add(new BasicNameValuePair("orderSiteIds", orderSiteIds));
//            goods_nvps.add(new BasicNameValuePair("orderTypes", orderTypes));
//            goods_nvps.add(new BasicNameValuePair("orderWareIds",productId_str));
//            goods_nvps.add(new BasicNameValuePair("orderWareTypes",orderWareTypes));
//
//            goods_name_post.setEntity(new UrlEncodedFormEntity(goods_nvps, "UTF-8"));
//            String goods_res = httpClient.execute(goods_name_post, responseHandler, localContext).toString();
//            if (StringUtils.isNotBlank(goods_res)){
//                goods_name_map.clear();
//                JSONArray goods_name_arr = JSON.parseArray(goods_res);
//                for (int i= 0 ; i < goods_name_arr.size() ; i++){
//                    JSONObject goods_obj = goods_name_arr.getJSONObject(i);
//                    String productId = goods_obj.getString("productId");
//                    String good_name = goods_obj.getString("name");
//                    goods_name_map.put(productId,good_name);
//                }
//            }
//        //设置商品名称
//            for(OrderInfo orderInfo : orderInfos){
//                String product_id = orderInfo.getProduct_id();
//                String goods_name = goods_name_map.get(product_id);
//                orderInfo.setGoods_name(goods_name);
//            }
//
//
//        return orderInfos;
//
//    }
//
//    private String listToString(List<String> stringList){
//        if (stringList==null) {
//            return null;
//        }
//        StringBuilder result=new StringBuilder();
//        boolean flag=false;
//        for (String string : stringList) {
//            if (flag) {
//                result.append(",");
//            }else {
//                flag=true;
//            }
//            result.append(string);
//        }
//        return result.toString();
//    }
//
//    private List<OrderInfo> getOrderByTime(String url, List<OrderInfo> orderInfos,List<String> orderIds,List<String> productIds)throws Exception {
//            HttpGet httpGet = HttpUtil.httpGet(url, "order.jd.com", "http://home.jd.com/");
//            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//            httpGet.setHeader("Connection", "keep-alive");
//            httpGet.setHeader("Cookie", params.get("login_cookie"));
//            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
//            String httpGet_res = httpClient.execute(httpGet, responseHandler, localContext).toString();
//
//            HtmlCleaner hc = new HtmlCleaner();
//            TagNode tn = hc.clean(httpGet_res);
//            String xpath = "//div[@id='order02']/div[@class='mc']/table/tbody";
//            Object[] orderList = null;
//            orderList = tn.evaluateXPath(xpath);
//            int len = orderList.length;
//            OrderInfo orderInfo = null;
//            String parent_no = "";
//            for (int i = 0; i < len; i++) {
//                orderInfo = new OrderInfo();
//                Object address = orderList[i];
//                TagNode orderTag = (TagNode) address;
//
//                String tbody_id = orderTag.getAttributeByName("id");
////                //说明 多个商品在同一个订单下面的
//                if (tbody_id.contains("parent")){
//                    continue;
//                }
//
//                Document dom =  new DomSerializer(new CleanerProperties()).createDOM(orderTag);
//                XPath xPath =  XPathFactory.newInstance().newXPath();
//                String exp = "//div[contains(@class,'goods-item')]";
//                NodeList nodeList =  (NodeList)xPath.evaluate(exp,dom,XPathConstants.NODESET);
//                Node node = nodeList.item(0);
//                Node classNode = node.getAttributes().getNamedItem("class");
//                String calssValue = classNode.getNodeValue();
//                String product_item = calssValue.split(" ")[1];
//                String product_id = product_item.split("-")[1];
//                orderInfo.setProduct_id(product_id);
//
//                productIds.add(product_id);
//
//
////                TagNode orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='goods-item']")[0];
////                String goods_item = orderTag1.getAttributeByName("class");
////                if (!StringUtils.isNotBlank(goods_item)){
////                    String product_item = goods_item.split(" ")[1];
////                    String product_id = product_item.split("-")[1];
////                    orderInfo.setProduct_id(product_id);
////
////                    productIds.add(product_id);
////                }
//
//                TagNode orderTag1 = (TagNode) orderTag.evaluateXPath("//span[@class='dealtime']")[0];
//                String order_time = orderTag1.getAttributeByName("title");
//                orderInfo.setOrder_time(DateUtils.parse(order_time));
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//span[@class='number']/a")[0];
//                String order_id = orderTag1.getText().toString();
//                orderInfo.setOrder_id(order_id);
//
//                orderIds.add(order_id);
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='p-name']/a")[0];
//                String goods_name  = orderTag1.getText().toString();
//                orderInfo.setGoods_name(goods_name);
//
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='goods-number']")[0];
//                String order_num = orderTag1.getText().toString().replace(" ", "").replace("x", "").replace("\r", "").replace("\n", "");
//                orderInfo.setGoods_num(order_num);
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='consignee tooltip']//div[@class='pc']/strong")[0];
//                String receiver = orderTag1.getText().toString().replace(" ", "");
//                orderInfo.setReceiver(receiver);
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='consignee tooltip']//div[@class='pc']/p")[0];
//                String harvest_address = orderTag1.getText().toString().replace(" ", "");
//                orderInfo.setHarvest_address(harvest_address);
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='amount']/span")[0];
//                String total_pay = orderTag1.getText().toString().replace(" ", "").replace("总额&yen;", "");
//                orderInfo.setTotal_pay(total_pay);
//
//                orderTag1 = (TagNode) orderTag.evaluateXPath("//div[@class='status']/span")[0];
//                String order_status = orderTag1.getText().toString().replace(" ", "").replace("\r", "").replace("\n", "");
//                orderInfo.setOrder_status(order_status);
//                orderInfo.setCreate_time(new Date());
//                orderInfo.setWebsite_name("jingdong");
//
//                orderInfos.add(orderInfo);
//            }
//        log.info("获取京东订单信息结束");
//        return orderInfos;
//    }
//
//    /**
//     * 获取收获地址
//     *
//     * @return
//     */
//    private void getAddr(JSONObject data) throws Exception {
//        log.info("获取京东收货地址信息开始");
//        List<AddrResult> addrResults = new ArrayList<AddrResult>();
//        //获取收获地址
//        String url_1 = "http://easybuy.jd.com/address/getEasyBuyList.action";
//        HttpPost httpPost1 = HttpUtil.httpPost(url_1, "easybuy.jd.com", "http://home.jd.com/");
//        httpPost1.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        httpPost1.setHeader("Accept-Encoding", "gzip, deflate");
//        httpPost1.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//        httpPost1.setHeader("Connection", "keep-alive");
//        httpPost1.setHeader("Cookie", params.get("login_cookie"));
//        httpPost1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        httpPost1.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
//        String httpPost1_res = httpClient.execute(httpPost1, responseHandler, localContext).toString();
//        HtmlCleaner hc = new HtmlCleaner();
//        TagNode tn = hc.clean(httpPost1_res);
//        String xpath = "//div[@id='addressList']/div[2]/div";
//        Object[] addressList = null;
//        addressList = tn.evaluateXPath(xpath);
//        int len = addressList.length;
//        for (int i = 0; i < len; i++) {
//            AddrResult addrResult = new AddrResult();
//            Object address = addressList[i];
//            TagNode addressesTag = (TagNode) address;
//            Object[] addressDatas = addressesTag.evaluateXPath("//div[1][@class='item-lcol']/div");
//            for (Object addressData : addressDatas) {
//                TagNode address_item = (TagNode) addressData;
//                List<TagNode> tagNodes = address_item.getChildTagList();
//                TagNode t1 = tagNodes.get(0);
//                String t1_v = t1.getText().toString();
//                TagNode t2 = tagNodes.get(1);
//                String t2_v = t2.getText().toString();
//                String key = t1_v.trim();
//                String value = t2_v.trim();
//                if (key.contains("收货人")) {
//                    addrResult.setConsignee_name(value);
//                } else if (key.contains("所在地区")) {
//                    addrResult.setConsignee_basic_addr(value);
//                } else if (key.contains("地址")) {
//                    addrResult.setConsignee_detail_addr(value);
//                } else if (key.contains("手机")) {
//                    addrResult.setConsignee_phone(value);
////                    String account = params.get("username");
//                    //matchPhone(account, value, addrResult, yiDongService, lianTongService, dianXinService);
//                }
//            }
//            addrResults.add(addrResult);
//        }
//        data.put("addrs", addrResults);
////        actionResult.setMessage(JSON.toJSONString(addrs));
//        log.info("获取京东收货地址信息结束");
//    }
//
//
//    /**
//     * 处理短信验证码
//     *
//     * @param login_res
//     */
//    private ActionResult moblieCodeProcess(JSONObject login_res, String login_url_page) throws Exception {
//        ActionResult actionResult = new ActionResult();
//        //https://safe.jd.com/dangerousVerify/index.action?username=sfdssdfsd&ReturnUrl=http://&p=sdfsdgsd
//        String venture = login_res.getString("venture");
//        String p = login_res.getString("p");
//        String ventureRet = login_res.getString("ventureRet");
//        //第一步  调用短信验证码 页面
//        String safe_url = "https://safe.jd.com/dangerousVerify/index.action?username=" + venture + "&ReturnUrl=" + ventureRet + "&p=" + p + "&t=" + System.currentTimeMillis();
//        params.put("safe_url", safe_url);
//
//        HttpGet safe_http_get = HttpUtil.httpGet(safe_url, "safe.jd.com", login_url_page);
//        safe_http_get.setHeader("Accept-Encoding", "gzip, deflate, br");
//        String safe_res = httpClient.execute(safe_http_get, responseHandler, localContext).toString();
//        //第二部 点击发送手机验证码
//        //https://safe.jd.com/dangerousVerify/getDownLinkCode?k=asdasfwerwe&t=12232423434
//        String sendMobileCode = getConfigValue(safe_res, "onclick=\"validMobile(.*?);\"", "");
//        if (!StringUtils.isEmpty(sendMobileCode)) {
//            sendMobileCode = sendMobileCode.substring(2, sendMobileCode.length() - 2);
//        }
//        params.put("sendMobileCode", sendMobileCode);
//        String sendValidUrl = "https://safe.jd.com/dangerousVerify/getDownLinkCode?k=" + sendMobileCode + "&t=" + System.currentTimeMillis();
//        HttpGet send_mobile_get = HttpUtil.httpGet(sendValidUrl, "safe.jd.com", safe_url);
//        send_mobile_get.setHeader("Accept-Encoding", "gzip, deflate, br");
//        String send_mobile_res = httpClient.execute(send_mobile_get, responseHandler, localContext).toString();
//        JSONObject send_mobile_obj = JSONObject.parseObject(send_mobile_res);
//        int send_mobile_resultCode = send_mobile_obj.getInteger("resultCode");
//        log.info("京东获取手机短信码返回结果：" + send_mobile_resultCode);
//        if (0 != send_mobile_resultCode) {
//            if (send_mobile_res.contains("发送验证码条数超过最大限制")) {
//                actionResult.setCode(ActionCode.GET_SMS_LIMIT_CODE);
//                return actionResult;
//            } else {
//                actionResult.setCode(ActionCode.SMS_CODE);
//                actionResult.setMessage("请输入短信验证码");
//                return actionResult;
//            }
//        } else {
//            log.debug("=================发送短信验证码返回===============：" + send_mobile_res);
//            //通知让用户填写验证码 数字
//            actionResult.setCode(ActionCode.SMS_CODE);
//            actionResult.setMessage("请输入短信验证码");
//            return actionResult;
//        }
//    }
//
//
//    /**
//     * 验证手机短信吗
//     *
//     * @param sendMobileCode
//     * @param safe_url
//     * @param code
//     * @throws Exception
//     */
//    private ActionResult checkMoblie(String sendMobileCode, String safe_url, String code) throws Exception {
//        ActionResult actionResult = new ActionResult();
//        //第三部，验证短信验证码是否正确
//        //https://safe.jd.com/dangerousVerify/checkDownLinkCode.action?code=251657&k=cba8e66a8be06ce18e0d62e3f89ccd31&t=1468131027023&eid=DD1B321578F982B618422AF0D3DCCDCC3561FE8D115030908EE62A88E6843286168C4FA79BF854B6AFC4A9A03ED49128&fp=90efa6774bd62474245933df69938a3e
//        String check_mobile_url = "https://safe.jd.com/dangerousVerify/checkDownLinkCode.action?code=" + code + "&k=" + sendMobileCode + "&t=" + System.currentTimeMillis();
//        HttpGet check_mobile_get = HttpUtil.httpGet(check_mobile_url, "safe.jd.com", safe_url);
//        check_mobile_get.setHeader("Accept-Encoding", "gzip, deflate, br");
//        String check_mobile_res = httpClient.execute(check_mobile_get, responseHandler, localContext).toString();
//        //{"returnUrl":"http:\/\/www.jd.com","pin":"网购go-go","resultCode":"0","rKey":"16335931","type":"down","resultMessage":"检验短信校验码成功"}
//        JSONObject check_mobile_obj = JSONObject.parseObject(check_mobile_res);
//        ///^1{1}[3,4,5,7,8]{1}\d{9}$/ 判断是否为数字的正则表达式
//        int check_resultCode = check_mobile_obj.getInteger("resultCode");
//        if (0 != check_resultCode) {
//            log.debug("==============================验证短信验证码返不正确，请核实正确重新填写==============================");
//            actionResult.setMessage(ActionCode.SMS_CODE_CHECK_ERROR_MESSAGE);
//            actionResult.setCode(ActionCode.SMS_CODE_CHECK_ERROR);
//            return actionResult;
//        } else {
//            log.debug("==============================验证短信验证码返正确==============================");
//            //http://home.jd.com/
//            String url = "http://home.jd.com/";
//            HttpPost httpPost = HttpUtil.httpPost(url, "home.jd.com", "http://www.jd.com/");
//            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
//            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//            httpPost.setHeader("Connection", "keep-alive");
//            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
//            List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps1, "UTF-8"));
//            String httpPost11 = httpClient.execute(httpPost, responseHandler, localContext).toString();
//        }
//        return actionResult;
//    }
//
//
//    /**
//     * 解析京东数据
//     *
//     * @param response_1
//     * @throws Exception
//     */
//    private void htmlCleaner(String response_1) throws Exception {
//        HtmlCleaner hc = new HtmlCleaner();
//        TagNode tn = hc.clean(response_1);
//        String xpath = "//form/input[@type='hidden']";
//        Object[] tagNodes = null;
//        tagNodes = tn.evaluateXPath(xpath);
//        for (Object tagNode : tagNodes) {
//            TagNode hidden_input = (TagNode) tagNode;
//            String name = hidden_input.getAttributeByName("name");
//            String value = hidden_input.getAttributeByName("value");
//            params.put(name, value);
//        }
//    }
//}

public class JDCrawler{
	
}