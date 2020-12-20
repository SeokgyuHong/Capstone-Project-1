const main = require('../main');
const {pool} = require('../secret_info/db_connect');

module.exports.data_from_sensor = (req,res)=>{
    const io = main.io; //메인페이지에서 생성한 모듈을 가지고 왔음
    let shoes_data = req.body.shoes_data ; //센서로 부터 받은 데이터
    let mac_address = req.body.mac_address;

    const nsp = io.of(mac);


}
