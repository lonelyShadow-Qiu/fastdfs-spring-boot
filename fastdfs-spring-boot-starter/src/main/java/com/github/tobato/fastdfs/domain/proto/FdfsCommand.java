package com.github.tobato.fastdfs.domain.proto;

import com.github.tobato.fastdfs.domain.conn.Connection;

/**
 * Fdfs交易命令抽象
 *
 * @author tobato
 */
public interface FdfsCommand<T> {


    /**
      * 执行交易
      *@param  conn-
      *@return T
      *@author qiuzulin
      *@date  2020/9/28
    */
    T execute(Connection conn);

}
