package utils;

import lombok.Getter;
import lombok.Setter;
import services.interfaces.IModel;

import java.util.List;

@Getter
@Setter
public class ResultList <T extends IModel>{
    Long count;
    List<T> resultList;

    public ResultList() {
    }

    public ResultList(Long count, List<T> resultList) {
        this.count = count;
        this.resultList = resultList;
    }
}
