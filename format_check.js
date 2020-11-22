//실사용 Foramt_check 함수
module.exports.e_mail_check = (e_mail)=>{ //이메일 정규식 체크 함수
    let e_mail_check = /^[0-9a-zA-z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;     
    if(!e_mail_check.test(e_mail)) //양식에 어긋날때
    {
        return false;
    }
    else
    {
        return true;
    }
}
module.exports.phone_check =(phone_number)=>{ //휴대폰 번호 및 일반전화 정규식 체크 함수
    let phone_number_check_1 = /^[0-9]{3}[-]+[0-9]{4}[-]+[0-9]{4}$/;     //휴대전화
    let phone_number_check_2 = /^\d{2,3}-\d{3,4}-\d{4}$/;  //일반전화 
    if(!phone_number_check_1.test(phone_number)&&!phone_number_check_2.test(phone_number)) //일반전화번호와 휴대폰번호 둘다 양식에서 어긋날경우
    {
        return false;
    }
    else
    {
        return true;
    }

}

module.exports.company_check = (company_number)=>{ //사업자번호 정규식 체크 함수
    let company_number_check = /^\d{3}-\d{2}-\d{3}$/;
    if(!company_number_check.test(company_number))
    {
        return false;
    }
    else
    {
        return true;
    }
}

// Wifi Mac address 포맷 체크
module.exports.checkValidMacAddress = (addr)=>{
    var re = /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/;
    //return re.test(addr);
    if(!re.test(addr))
    {
        return false;
    }
    else
    {
        return true;
    }
};