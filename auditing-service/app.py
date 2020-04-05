import json
import os

from elasticsearch import Elasticsearch
from flask import Flask, jsonify

app = Flask(__name__)
es_service = Elasticsearch(hosts=[os.environ.get("ES_HOST", default="localhost:9200")])


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/parcels')
def get_parcels():
    results = es_service.search(index="parcel.outbox", doc_type='tracking', body={"query": {"match_all": {}}})
    count = results['hits']['total']['value']
    parcels = list(map(lambda x: json.loads(x['_source']['payload']), results['hits']['hits']))
    return jsonify(
        {
            'count': count,
            'parcels': parcels
        }
    )


if __name__ == '__main__':
    app.run(host='0.0.0.0')
