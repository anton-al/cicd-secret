#Create a secret
aws secretsmanager create-secret \
    --name MyTestSecret \
    --description "My test secret created with the CLI." \
    --secret-string "{\"user\":\"diegor\",\"password\":\"EXAMPLE-PASSWORD\"}"

aws secretsmanager create-secret \
    --name MyTestSecret \
    --secret-string file://mycreds.json

# Modify a secret
aws secretsmanager update-secret \
    --secret-id MyTestSecret \
    --description "This is a new description for the secret."

aws secretsmanager put-secret-value \
    --secret-id MyTestSecret \
    --secret-string "{\"user\":\"diegor\",\"password\":\"EXAMPLE-PASSWORD\"}"

aws secretsmanager put-secret-value \
    --secret-id MyTestSecret \
    --secret-string file://mycreds.json

# Find secrets
aws secretsmanager list-secrets

aws secretsmanager list-secrets \
    --filter Key="name",Values="Test"

# Delete a secret
aws secretsmanager delete-secret \
    --secret-id MyTestSecret \
    --recovery-window-in-days 7

aws secretsmanager delete-secret \
    --secret-id MyTestSecret \
    --force-delete-without-recovery

# Restore a previously deleted secret
aws secretsmanager restore-secret \
    --secret-id MyTestSecret

### REPLICAS ###

# Replicate a secret to another region
aws secretsmanager replicate-secret-to-regions \
    --secret-id MyTestSecret \
    --add-replica-regions Region=eu-west-3

# Delete a replica secret
aws secretsmanager remove-regions-from-replication \
    --secret-id MyTestSecret \
    --remove-replica-regions eu-west-3

# Promote a replica secret to a primary
aws secretsmanager stop-replication-to-replica \
    --secret-id MyTestSecret


### TAG ###

# Add a tag to a secret
aws secretsmanager tag-resource \
            --secret-id MyTestSecret \
            --tags Key=FirstTag,Value=FirstValue

# Add multiple tags to a secret
aws secretsmanager tag-resource \
            --secret-id MyTestSecret \
            --tags '[{"Key": "FirstTag", "Value": "FirstValue"}, {"Key": "SecondTag", "Value": "SecondValue"}]'

# Remove tags from a secret
aws secretsmanager untag-resource \
            --secret-id MyTestSecret \
            --tag-keys '[ "FirstTag", "SecondTag"]'