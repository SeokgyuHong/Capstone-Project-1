const format = require('../local_lib/format_check');

const {pool} = require('../secret_info/db_connect');




module.exports.sensor_deletion = (req,res)=>{
    let wifi_mac_address = req.body.wifi_mac_address;//wifi맥주소

    if(!format.checkValidMacAddress(wifi_mac_address))
    {
        res.send({'key':1}) //센서 맥주소 양식 에러 
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`delete from Sensor where  wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
            console.log('센서삭제성공');
            res.send({'key':2}) //센서삭제성공 
        }).catch((err)=>{
            console.log('센서삭제시 에러발생1'+err);
            res.send({'key':0,'err_code':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log('센서삭제시 에러발생2'+err);
        res.send({'key':0,'err_code':err.code});
    })
}