package services.interfaces;

import utils.ResultList;

import java.util.ArrayList;
import java.util.List;

public interface IServices <T extends IModel>{
    public void Insert(T t) ;

    public void Update(T t) ;

    public void Delete(int t) ;

    public ResultList<T> selectAll(int start, int elementPerPages) ;

    public T selectedById(int t) ;

}
