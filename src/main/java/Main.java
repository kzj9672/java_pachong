
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import top.sellet.entity.Movie;
import top.sellet.mapper.MovieMapper;
import top.sellet.utils.GetJson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author mo
 */
public class Main {
    public static void main(String[] args) {
        String resource = "sqlMapConfig.xml";
        InputStream inputStream=null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSessionFactory sqlSession = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession openSession = sqlSession.openSession();
        MovieMapper movieMapper = openSession.getMapper(MovieMapper.class);
        int start;
        int total = 0;
        int end = 100;
        for (start=0;start<=end;start+=20){
            String address="https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=&start="+start;
            try {
                JSONObject dayLine = new GetJson().getHttpJson(address, 1);
                System.out.println("start:"+start);
                JSONArray json = dayLine.getJSONArray("data");
                List<Movie> list = JSON.parseArray(json.toString(), Movie.class);
                if (start <= end){
                    System.out.println("已经爬取到底了");

                }
                for (Movie movie:list){
                    movieMapper.insert(movie);
                    openSession.commit();
                }
                total+=list.size();
                System.out.print("正在爬取中---共抓取："+total+"条数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
