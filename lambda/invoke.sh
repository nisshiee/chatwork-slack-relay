aws lambda invoke \
--region ap-northeast-1 \
--function-name chatwork-lambda-test \
--payload '"あいうえお"' \
--invocation-type RequestResponse ./target/test-result
