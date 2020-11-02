const mariadb = require('mariadb');
const {config} = require('./db_loginfo');


var info = config();

module.exports.pool = mariadb.createPool({ //데이터 베이스 연결 정보 저장 query pool형식으로 만들어서 할당 -> 쿼리 작성 -> 해제 순으로 동작
    host : info.host,
    port: info.port,
    user :info.user,
    password :info.password,
    database: info.database,
    connectionLimit : 5
});
