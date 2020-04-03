import json

from elasticsearch import Elasticsearch
from flask import Flask, jsonify

app = Flask(__name__)
es_service = Elasticsearch(hosts=['localhost:9200'])


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/parcels')
def get_parcels():
    results = es_service.search(index="parcel_created", doc_type='tracking', body={"query": {"match_all": {}}})
    parcels = list(map(lambda x: json.loads(x['_source']['payload']), results['hits']['hits']))
    return jsonify(
        {
            'count': results['hits']['total']['value'],
            'parcels': parcels
        }
    )


if __name__ == '__main__':
    app.run()
