package cn.ocoop.framework.jdbc.shard;

import cn.ocoop.framework.jdbc.datasource.NamedDataSource;
import com.google.common.collect.Lists;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by liolay on 2017/12/5.
 */
public class DataSourceHolder {
    private Map<String, DataSource> dataSources;

    public DataSourceHolder(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }


    public List<NamedDataSource> route(String staticSql) {
        List<NamedDataSource> targetDataSources = Lists.newArrayList();
        for (Map.Entry<String, DataSource> dataSourceEntry : dataSources.entrySet()) {
            targetDataSources.add(new NamedDataSource(dataSourceEntry.getKey(), dataSourceEntry.getValue()));
        }
        return targetDataSources;
    }

    public List<NamedDataSource> route(Statement statement) {
        List<NamedDataSource> targetDataSources = Lists.newArrayList();
        for (Map.Entry<String, DataSource> dataSourceEntry : dataSources.entrySet()) {
            targetDataSources.add(new NamedDataSource(dataSourceEntry.getKey(), dataSourceEntry.getValue()));
        }
        return targetDataSources;
    }

    public List<NamedDataSource> route(PreparedStatement preparedStatement) {
        List<NamedDataSource> targetDataSources = Lists.newArrayList();
        for (Map.Entry<String, DataSource> dataSourceEntry : dataSources.entrySet()) {
            targetDataSources.add(new NamedDataSource(dataSourceEntry.getKey(), dataSourceEntry.getValue()));
        }
        return targetDataSources;
    }
}
