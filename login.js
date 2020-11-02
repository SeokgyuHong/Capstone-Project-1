
const {log_info} = require('./db_loginfo.js');
const {insert} = require('./db_sql.js');

module.exports.save_login_info = (body)=>{
    var name = body.name;
    var age = body.age;
    var gender = body.gender;
    var email = body.email;
    var birthday = body.birthday;
    
    console.log(name,age,gender,email,birthday)
    insert(body)
    log_info();
}