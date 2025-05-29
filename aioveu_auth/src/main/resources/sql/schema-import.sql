# 基本数据导入
insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`) values
('aioveu-admin','','$2a$10$5wSiXVHHRBZqv2jJflg37ORILo03ESFwGLoKfNEDdqDxsgU3jyKCe','read','password,authorization_code,refresh_token','http://manager-api.fan.com:5001/manager-api/oauth/callback',NULL,3600,86400,NULL,'true'),
('aioveu-gateway','aioveu-gateway-server','$2a$10$5wSiXVHHRBZqv2jJflg37ORILo03ESFwGLoKfNEDdqDxsgU3jyKCe','write',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
    