package com.longer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.longer.es.pojo.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ElasticsearchTest {

    @Test// 创建客户端
    public void elasticsearchTest() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        esClient.close();// 关闭 es 客户端
    }

    @Test// 创建索引
    public void esTestCreateIndex() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest("user");

        // 响应对象
        CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);

        System.out.println("索引创建成功了吗：" + response.isAcknowledged());// 响应状态

        esClient.close();// 关闭 es 客户端
    }

    @Test// 查询索引
    public void esTestSearchIndex() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询索引
        GetIndexRequest request = new GetIndexRequest("user");

        // 响应对象
        GetIndexResponse response = esClient.indices().get(request, RequestOptions.DEFAULT);

        // 响应状态
        System.out.println(response.getAliases());
        System.out.println(response.getMappings());
        System.out.println(response.getSettings());

        esClient.close();// 关闭 es 客户端
    }

    @Test// 删除索引
    public void esTestDeleteIndex() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询索引
        DeleteIndexRequest request = new DeleteIndexRequest("shopping");
        AcknowledgedResponse response = esClient.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println("删除成功了吗：" + response.isAcknowledged());// 响应状态

        esClient.close();// 关闭 es 客户端
    }

    @Test// 向索引中添加数据
    public void esTestIndexInsert() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 插入数据
        IndexRequest request = new IndexRequest();
        request.index("user").id("101");// index 是要添加的索引名称，id 是生成的主键

        // 准备要添加的对象
        User user = new User("小糸侑", "女", 16);

        // 向 es 插入数据，数据必须是 json 格式的
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);// 将对象转换成 json 字符串
        request.source(userJson, XContentType.JSON);

        // 响应对象
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);

        System.out.println(response.getResult());// 响应状态

        esClient.close();// 关闭 es 客户端
    }

    @Test// 局部修改
    public void esTestDocumentUpdate() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 修改数据
        UpdateRequest request = new UpdateRequest();
        request.index("user").id("101");// index 是要添加的索引名称，id 是生成的主键
        request.doc(XContentType.JSON, "age", 18);// 修改的数据

        // 响应对象
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);

        System.out.println(response.getResult());// 响应状态

        esClient.close();// 关闭 es 客户端
    }

    @Test// 文档查询
    public void esTestDocumentSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询数据
        GetRequest request = new GetRequest();
        request.index("user").id("101");

        // 响应对象
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);

        // 响应结果
        System.out.println(response.getSourceAsString());

        esClient.close();// 关闭 es 客户端
    }

    @Test// 文档删除
    public void esTestDocumentDelete() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 删除数据
        DeleteRequest request = new DeleteRequest();
        request.index("user").id("101");

        // 响应对象
        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);

        // 响应结果
        System.out.println(response.toString());

        esClient.close();// 关闭 es 客户端
    }

    @Test// 批量插入数据
    public void esTestDocumentBulkInsert() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 批量处理数据请求
        BulkRequest request = new BulkRequest();

        // 批量插入数据
        request.add(new IndexRequest().index("user").id("101").source(XContentType.JSON, "name", "小糸侑", "sex", "女", "age", 16));
        request.add(new IndexRequest().index("user").id("102").source(XContentType.JSON, "name", "七海灯子", "sex", "女", "age", 17));
        request.add(new IndexRequest().index("user").id("103").source(XContentType.JSON, "name", "佐伯纱耶香", "sex", "女", "age", 17));
        request.add(new IndexRequest().index("user").id("104").source(XContentType.JSON, "name", "我妻由乃", "sex", "女", "age", 16));
        request.add(new IndexRequest().index("user").id("105").source(XContentType.JSON, "name", "龙宫礼奈", "sex", "女", "age", 15));
        request.add(new IndexRequest().index("user").id("106").source(XContentType.JSON, "name", "前原圭一", "sex", "男", "age", 15));
        request.add(new IndexRequest().index("user").id("107").source(XContentType.JSON, "name", "园崎诗音", "sex", "女", "age", 16));
        request.add(new IndexRequest().index("user").id("108").source(XContentType.JSON, "name", "园崎魅音", "sex", "女", "age", 16));
        request.add(new IndexRequest().index("user").id("109").source(XContentType.JSON, "name", "北条沙都子", "sex", "女", "age", 12));
        request.add(new IndexRequest().index("user").id("110").source(XContentType.JSON, "name", "古手梨花", "sex", "女", "age", 12));

        // 响应对象
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);

        System.out.println(response.getTook());// 执行时间
        System.out.println(response.getItems().toString());// 响应结果

        esClient.close();// 关闭 es 客户端
    }

    @Test// 批量删除数据
    public void esTestDocumentBulkDelete() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 批量处理数据请求
        BulkRequest request = new BulkRequest();

        // 批量删除数据
        request.add(new DeleteRequest().index("user").id("101"));
        request.add(new DeleteRequest().index("user").id("102"));
        request.add(new DeleteRequest().index("user").id("103"));

        // 响应对象
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);

        System.out.println(response.getTook());// 执行时间
        System.out.println(response.getItems());// 响应结果

        esClient.close();// 关闭 es 客户端
    }

    @Test// 全量查询
    public void esTestDocumentSelectAll() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));// 全量查询 matchAllQuery()

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 条件查询
    public void esTestDocumentTermSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("sex", "女")));// 条件查询 termQuery()

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 分页查询 & 排序 & 查询字段
    public void esTestDocumentSelectOr() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.from(0);// 分页查询的起始页
        builder.size(3);// 每页查询多少条记录
        builder.sort("age", SortOrder.DESC);// 降序排序

        // 查询字段
        String[] includes = {"name", "age"};// 查询结果中包含的字段
        String[] excludes = {};// 查询结果中排除的字段
        builder.fetchSource(includes, excludes);// 将条件放入

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 组合查询
    public void esTestDocumentGroupSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 组合查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // must 类似数据库中的 AND java 中的 &&
        // boolQueryBuilder.must(QueryBuilders.matchQuery("age", 16));
        // boolQueryBuilder.must(QueryBuilders.matchQuery("sex", "女"));

        // mustNot 不是必须的，相当于取反
        // boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "女"));

        // should 类似数据库中的 OR java 中的 ||
        boolQueryBuilder.should(QueryBuilders.matchQuery("sex", "男"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("sex", "女"));

        builder.query(boolQueryBuilder);// 将组合条件放入

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 范围查询
    public void esTestDocumentScopeSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 范围查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");

        // gte 大于等于 gt 大于 lte 小于等于 lt 小于
        rangeQueryBuilder.gte(15);
        rangeQueryBuilder.lt(17);

        builder.query(rangeQueryBuilder);// 将组合条件放入

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 模糊查询
    public void esTestDocumentVagueSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(QueryBuilders.fuzzyQuery("name", "园崎").fuzziness(Fuzziness.ONE));// 模糊查询 Fuzziness.ONE 允许的范围

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 高亮查询
    public void esTestDocumentHighlightSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 生成查询的条款
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("name", "糸");

        // 生成高亮对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");// 设定前缀
        highlightBuilder.postTags("</font>");// 设定后缀
        highlightBuilder.field("name");// 标记为高亮的字段

        builder.highlighter(highlightBuilder);// 将生成高亮的对象放入
        builder.query(termsQueryBuilder);// 将查询的条款放入生成条件的对象

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

    @Test// 聚合查询 & 分组查询（需要日志查看）
    public void esTestDocumentAggregationSelect() throws IOException {
        // 创建 es 客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        // 查询请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");// 要查询的索引对象

        // 生成查询条件的对象
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 聚合查询
        // AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");// 最大值
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");// 分组

        builder.aggregation(aggregationBuilder);// 将生成的聚合查询条件放入

        request.source(builder);// 将生成条件的对象放入

        // 响应对象
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        // 搜索到的匹配记录
        SearchHits hits = response.getHits();

        System.out.println(hits.getTotalHits());// 全部的数据条数
        System.out.println(response.getTook());// 执行的时间

        // 将每条数据遍历
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        esClient.close();// 关闭 es 客户端
    }

}
