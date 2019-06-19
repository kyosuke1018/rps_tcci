package com.tcci.ec.model.statistic;

import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class TimeSeriesDataVO extends BaseResponseVO implements Serializable {
    private List<String> timeSeriesLabels;
    private List<String> datasetLabels;
    private List<List<BigDecimal>> datasets;
    private boolean nodata;

    public boolean isNodata() {
        return nodata;
    }

    public void setNodata(boolean nodata) {
        this.nodata = nodata;
    }

    public List<String> getTimeSeriesLabels() {
        return timeSeriesLabels;
    }

    public void setTimeSeriesLabels(List<String> timeSeriesLabels) {
        this.timeSeriesLabels = timeSeriesLabels;
    }

    public List<List<BigDecimal>> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<List<BigDecimal>> datasets) {
        this.datasets = datasets;
    }

    public List<String> getDatasetLabels() {
        return datasetLabels;
    }

    public void setDatasetLabels(List<String> datasetLabels) {
        this.datasetLabels = datasetLabels;
    }
    
    
}
